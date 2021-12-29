package com.edcode.commerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.account.AddressInfo;
import com.edcode.commerce.account.BalanceInfo;
import com.edcode.commerce.common.TableId;
import com.edcode.commerce.dao.ScaCommerceOrderDao;
import com.edcode.commerce.entity.ScaCommerceOrder;
import com.edcode.commerce.feign.AddressClient;
import com.edcode.commerce.feign.NotSecuredBalanceClient;
import com.edcode.commerce.feign.NotSecuredGoodsClient;
import com.edcode.commerce.feign.SecuredGoodsClient;
import com.edcode.commerce.filter.AccessContext;
import com.edcode.commerce.goods.DeductGoodsInventory;
import com.edcode.commerce.goods.SimpleGoodsInfo;
import com.edcode.commerce.order.LogisticsMessage;
import com.edcode.commerce.order.OrderInfo;
import com.edcode.commerce.service.IOrderService;
import com.edcode.commerce.source.LogisticsSource;
import com.edcode.commerce.vo.PageSimpleOrderDetail;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 订单相关服务接口实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
@EnableBinding(LogisticsSource.class) // SpringCloud stream
public class OrderServiceImpl implements IOrderService {

	private final ScaCommerceOrderDao scaCommerceOrderDao;

	/**
	 * Feign 客户端
	 */
	private final AddressClient addressClient;
	private final SecuredGoodsClient securedGoodsClient;
	private final NotSecuredBalanceClient notSecuredBalanceClient;
	private final NotSecuredGoodsClient notSecuredGoodsClient;

	/**
	 * SpringCloud Stream 发射器
	 */
	private final LogisticsSource logisticsSource;

	/**
	 * 创建订单：这里会涉及到分布式事务
     *      创建订单会涉及到多个步骤和校验，当不满足情况时直接抛出异常；
     *      0.  获取地址信息
     *      1.  校验请求对象是否合法
     *      2.  创建订单
     *      3.  扣减商品库存
     *      4.  扣减用户账户余额
     *      5.  发送订单物流消息 SpringCloud Stream + kafka
	 *
	 * 每个环境出错，都抛出 RuntimeException 异常是为了 Seata 分布式事务回滚
	 * 
	 * @param orderInfo 订单信息
	 * @return TableId
	 */
	@GlobalTransactional(rollbackFor = Exception.class)
	@Override
	public TableId createOrder(OrderInfo orderInfo) {

	    // 0.  获取地址信息
        AddressInfo addressInfo = addressClient.getAddressInfoByTablesId(
                new TableId(Collections.singletonList(
                        // 根据 id 查询地址信息
                        new TableId.Id(orderInfo.getUserAddressId()))
                )).getData();

        // 1.  校验请求对象是否合法（商品信息不需要校验，扣减库存时候会校验）
		if (CollectionUtils.isEmpty(addressInfo.getAddressItems())) {
			throw new RuntimeException("用户地址不存在：" + orderInfo.getUserAddressId());
		}

        // 2.  创建订单
        ScaCommerceOrder newOrder = scaCommerceOrderDao.save(
                ScaCommerceOrder.builder()
                        .userId(AccessContext.getLoginUserInfo().getId())
                        .addressId(orderInfo.getUserAddressId())
                        .orderDetail(JSON.toJSONString(orderInfo.getOrderItems()))
                        .build()
        );
		log.info("创建订单成功：[{}], [{}]", AccessContext.getLoginUserInfo().getId(), newOrder.getId());

        // 3.  扣减商品库存
        Boolean b = notSecuredGoodsClient.deductGoodsInventory(
                orderInfo.getOrderItems()
                        .stream()
                        .map(OrderInfo.OrderItem::toDeductGoodsInventory)
                        .collect(Collectors.toList())
        ).getData();

		if (Boolean.FALSE.equals(b)) {
            throw new RuntimeException("扣减商品库存失败");
        }

		// 4.  扣减用户账户余额
        // 4.1  获取商品信息，计算总价格
        List<SimpleGoodsInfo> goodsInfos = notSecuredGoodsClient.getSimpleGoodsInfoByTableId(
                new TableId(
                        orderInfo.getOrderItems()
                                .stream()
                                .map(o -> new TableId.Id(o.getGoodsId()))
                                .collect(Collectors.toList())
                )
        ).getData();
		// 将商品信息变成 Map 格式
		Map<Long, SimpleGoodsInfo> goodsId2GoodsInfo = goodsInfos.stream()
				.collect(Collectors.toMap(SimpleGoodsInfo::getId, Function.identity()));

		long balance = 0;
		for (OrderInfo.OrderItem orderItem : orderInfo.getOrderItems()) {
		    // 累加 += 单个商品价格 * 购买的个数   -->  获取单项
            balance += goodsId2GoodsInfo.get(orderItem.getGoodsId()).getPrice() * orderItem.getCount();
            System.out.println("com.edcode.commerce.service.impl.OrderServiceImpl.createOrder.balance: " + balance);
		}
        assert balance > 0;

        // 4.2  填写总价格，扣减账户余额
        BalanceInfo balanceInfo = notSecuredBalanceClient.deductBalance(
                new BalanceInfo(AccessContext.getLoginUserInfo().getId(), balance)
        ).getData();
        if (null == balanceInfo) {
            throw new RuntimeException("扣除用户余额失败");
        }
		log.info("扣减用户余额：[{}],[{}]", newOrder.getId(), JSON.toJSONString(balanceInfo));


        // 5.  发送订单物流消息 SpringCloud Stream + kafka
        LogisticsMessage logisticsMessage = new LogisticsMessage(
                AccessContext.getLoginUserInfo().getId(),
                newOrder.getId(),
                orderInfo.getUserAddressId(),
                // 没有备注信息
                null
        );
        boolean sendBoolean = logisticsSource.logisticsOutput().send(
                MessageBuilder.withPayload(JSON.toJSONString(logisticsMessage)).build()
        );

        if (Boolean.FALSE.equals(sendBoolean)) {
            throw new RuntimeException("发送物流信息失败！");
        }
		log.info("发送创建订单信息到 Kafka：[{}]", JSON.toJSONString(logisticsMessage));

        // 返回 TableId - 订单ID
        return new TableId(
                Collections.singletonList(
                        new TableId.Id(newOrder.getId())
                )
        );
	}

	// 获取当前用户的订单信息: 带有分页
	@Override
	public PageSimpleOrderDetail getSimpleOrderDetailByPage(int page) {

		if (page <= 0) {
			// 默认是第一页
			page = 1;
		}

		// 这里分页的规则是：1页 10条数据，按照 id 倒序排序

		// package org.springframework.data.domain
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());

		Page<ScaCommerceOrder> orderPage = scaCommerceOrderDao.findAllByUserId(
				AccessContext.getLoginUserInfo().getId(),
				pageable
		);
		List<ScaCommerceOrder> orders = orderPage.getContent();

		// 如果是空， 直接返回空数组
		if (CollectionUtils.isEmpty(orders)) {
			return new PageSimpleOrderDetail(Collections.emptyList(),false);
		}

		// 获取当前订单中所有的 goodsId 这个 set 不可能为空或者是 null, 否则，代码一定有 bug
		Set<Long> goodsIdsInOrders = new HashSet<>();
		orders.forEach(o -> {
			List<DeductGoodsInventory> goodsAndCount = JSON.parseArray(
					o.getOrderDetail(), DeductGoodsInventory.class
			);
			goodsIdsInOrders.addAll(goodsAndCount.stream()
					.map(DeductGoodsInventory::getGoodsId)
					.collect(Collectors.toSet()));
		});

		assert CollectionUtils.isNotEmpty(goodsIdsInOrders);

		// 是否还有更多页： 总页面是否大于当前给定的页
		boolean hasMore = orderPage.getTotalPages() > page;

		// 获取商品信息
		// securedGoodsClient 是安全的，做兜底。保证接口不抛出异常
		List<SimpleGoodsInfo> goodsInfos = securedGoodsClient.getSimpleGoodsInfoByTableId(
				new TableId(goodsIdsInOrders.stream()
						.map(TableId.Id::new)
						.collect(Collectors.toList()))
		).getData();

		// 获取地址信息
		AddressInfo addressInfo = addressClient.getAddressInfoByTablesId(
				new TableId(orders.stream()
						.map(o -> new TableId.Id(o.getAddressId()))
						.distinct()
						.collect(Collectors.toList()))
		).getData();

		// 组装订单中的商品, 地址信息 -> 订单信息
		return new PageSimpleOrderDetail(
				assembleSimpleOrderDetail(orders, goodsInfos, addressInfo),
				hasMore
		);

	}

	/**
	 * 组装订单详情
	 *
	 * @return
	 */
	private List<PageSimpleOrderDetail.SingleOrderItem> assembleSimpleOrderDetail(
			List<ScaCommerceOrder> orders,
			List<SimpleGoodsInfo> goodsInfos,
			AddressInfo addressInfo) {

		// goodsId -> SimpleGoodsInfo
		Map<Long, SimpleGoodsInfo> id2GoodsInfo = goodsInfos.stream()
				// 使用Stream时，要将它转换成其他容器或Map。这时候，就会使用到Function.identity()
				.collect(Collectors.toMap(SimpleGoodsInfo::getId, Function.identity()));
		// addressId -> AddressInfo.AddressItem
		Map<Long, AddressInfo.AddressItem> id2AddressItem = addressInfo.getAddressItems()
				.stream().collect(
						// t -> t 等价 Function.identity()
						Collectors.toMap(AddressInfo.AddressItem::getId, t -> t)
				);

		List<PageSimpleOrderDetail.SingleOrderItem> result = new ArrayList<>(orders.size());
		orders.forEach(o -> {

			PageSimpleOrderDetail.SingleOrderItem orderItem = new PageSimpleOrderDetail.SingleOrderItem();
			orderItem.setId(o.getId());
			// getOrDefault() 当Map集合中有这个key时，就使用这个key对应的value值，如果没有就使用默认值defaultValue
			orderItem.setUserAddress(id2AddressItem.getOrDefault(o.getAddressId(), new AddressInfo.AddressItem(-1L)).toUserAddress());
			orderItem.setGoodsItems(buildOrderGoodsItem(o, id2GoodsInfo));

			result.add(orderItem);
		});

		return result;
	}

	/**
	 * 构造订单中的商品信息
	 *
	 * @param order
	 * @param id2GoodsInfo
	 * @return
	 */
	private List<PageSimpleOrderDetail.SingleOrderGoodsItem> buildOrderGoodsItem(ScaCommerceOrder order, Map<Long, SimpleGoodsInfo> id2GoodsInfo){
		List<PageSimpleOrderDetail.SingleOrderGoodsItem> goodsItems = new ArrayList<>();
		List<DeductGoodsInventory> goodsAndCount = JSON.parseArray(
				order.getOrderDetail(), DeductGoodsInventory.class
		);

		goodsAndCount.forEach(gc -> {
			PageSimpleOrderDetail.SingleOrderGoodsItem goodsItem = new PageSimpleOrderDetail.SingleOrderGoodsItem();
			goodsItem.setCount(gc.getCount());
			goodsItem.setSimpleGoodsInfo(id2GoodsInfo.getOrDefault(gc.getGoodsId(), new SimpleGoodsInfo(-1L)));

			goodsItems.add(goodsItem);
		});

		return goodsItems;
	}

}
