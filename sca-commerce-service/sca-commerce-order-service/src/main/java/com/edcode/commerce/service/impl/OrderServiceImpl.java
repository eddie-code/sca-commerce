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
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

	@Override
	public PageSimpleOrderDetail getSimpleOrderDetailByPage(int page) {
		return null;
	}
}
