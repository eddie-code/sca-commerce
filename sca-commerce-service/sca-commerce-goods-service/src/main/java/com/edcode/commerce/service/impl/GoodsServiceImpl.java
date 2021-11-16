package com.edcode.commerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.common.TableId;
import com.edcode.commerce.constant.GoodsConstant;
import com.edcode.commerce.dao.ScaCommerceGoodsDao;
import com.edcode.commerce.entity.ScaCommerceGoods;
import com.edcode.commerce.goods.DeductGoodsInventory;
import com.edcode.commerce.goods.GoodsInfo;
import com.edcode.commerce.goods.SimpleGoodsInfo;
import com.edcode.commerce.service.IGoodsService;
import com.edcode.commerce.vo.PageSimpleGoodsInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 商品微服务相关服务功能实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class GoodsServiceImpl implements IGoodsService {

    private final StringRedisTemplate redisTemplate;

    private final ScaCommerceGoodsDao scaCommerceGoodsDao;

    /**
     * 根据 TableId 查询商品详细信息
     */
    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {

//        List<Long> idLong = new ArrayList<>();
//        List<TableId.Id> idIds = tableId.getIds();
//        for (TableId.Id tid: idIds) {
//            idLong.add(tid.getId());
//        }
//        log.info("通过 ids 获取商品信息: [{}]", JSON.toJSONString(idLong));

        // 详细的商品信息, 不能从 redis cache 中去拿
        List<Long> ids = tableId.getIds().stream()
//                .map(e->e.getId())
                .map(TableId.Id::getId)
                .collect(Collectors.toList());
        log.info("通过 ids 获取商品信息: [{}]", JSON.toJSONString(ids));

        // findAllById 返回类型：Iterable， 所以使用 IterableUtils.toList 转换 List
        List<ScaCommerceGoods> scaCommerceGoods = IterableUtils.toList(
                // 查数据库
                scaCommerceGoodsDao.findAllById(ids)
        );

        return scaCommerceGoods.stream()
//                .map(e -> e.toGoodsInfo())
                .map(ScaCommerceGoods::toGoodsInfo)
                .collect(Collectors.toList());
    }

    /**
     * 获取分页的商品信息
     */
    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page) {

        // 分页不能从 redis cache 中去拿
        if (page <= 1) {
            page = 1;   // 默认是第一页
        }

        // 这里分页的规则(你可以自由修改): 1页10调数据, 按照 id 倒序排列
        Pageable pageable = PageRequest.of(
                page - 1, 10, Sort.by("id").descending()
        );
        Page<ScaCommerceGoods> orderPage = scaCommerceGoodsDao.findAll(pageable);

        // 是否还有更多页: 总页数是否大于当前给定的页
        boolean hasMore = orderPage.getTotalPages() > page;

        return new PageSimpleGoodsInfo(
                orderPage.getContent().stream()
                        .map(ScaCommerceGoods::toSimple)
                        .collect(Collectors.toList()),
                hasMore
        );
    }

    /**
     * 根据 TableId 查询简单商品信息
     */
    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {
        // 获取商品的简单信息, 可以从 redis cache 中去拿, 拿不到需要从 DB 中获取并保存到 Redis 里面
        // Redis 中的 KV 都是字符串类型
        List<Object> goodIds = tableId.getIds().stream()
                .map(i -> i.getId().toString()).collect(Collectors.toList());

        // FIXME 如果 cache 中查不到 goodsId 对应的数据, 返回的是 null, [null, null]
        List<Object> cachedSimpleGoodsInfos = redisTemplate.opsForHash()
                .multiGet(GoodsConstant.SCACOMMERCE_GOODS_DICT_KEY, goodIds)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 如果从 Redis 中查到了商品信息, 分两种情况去操作
        if (CollectionUtils.isNotEmpty(cachedSimpleGoodsInfos)) {
            // 1. 如果从缓存中查询出所有需要的 SimpleGoodsInfo
            if (cachedSimpleGoodsInfos.size() == goodIds.size()) {
                log.info("通过ID获取简单商品信息（从缓存）: [{}]",
                        JSON.toJSONString(goodIds));
                return parseCachedGoodsInfo(cachedSimpleGoodsInfos);
            } else {
                // 2. 一半从数据表中获取 (right), 一半从 redis cache 中获取 (left)
                List<SimpleGoodsInfo> left = parseCachedGoodsInfo(cachedSimpleGoodsInfos);
                // 取差集: 传递进来的参数 - 缓存中查到的 = 缓存中没有的
                Collection<Long> subtractIds = CollectionUtils.subtract(
                        goodIds.stream()
                                .map(g -> Long.valueOf(g.toString()))
                                .collect(Collectors.toList()),
                        left.stream()
                                .map(SimpleGoodsInfo::getId)
                                .collect(Collectors.toList())
                );

                // 缓存中没有的, 查询数据表并缓存
                List<SimpleGoodsInfo> right = queryGoodsFromDBAndCacheToRedis(
                        new TableId(subtractIds.stream()
                                .map(TableId.Id::new)
                                .collect(Collectors.toList())
                        )
                );
                // 合并 left 和 right 并返回
                log.info("通过ID（从数据库和缓存）获取简单商品信息: [{}]", JSON.toJSONString(subtractIds));
                return new ArrayList<>(CollectionUtils.union(left, right));
            }
        } else {
            // 从 redis 里面什么都没有查到
            return queryGoodsFromDBAndCacheToRedis(tableId);
        }
    }

    /**
     * 将缓存中的数据反序列化成 Java Pojo 对象
     * */
    private List<SimpleGoodsInfo> parseCachedGoodsInfo(List<Object> cachedSimpleGoodsInfo) {

        return cachedSimpleGoodsInfo.stream()
                .map(s -> JSON.parseObject(s.toString(), SimpleGoodsInfo.class))
                .collect(Collectors.toList());
    }

    /**
     * 从数据表中查询数据, 并缓存到 Redis 中
     * */
    private List<SimpleGoodsInfo> queryGoodsFromDBAndCacheToRedis(TableId tableId) {

        // 从数据表中查询数据并做转换
        List<Long> ids = tableId.getIds().stream()
                .map(TableId.Id::getId).collect(Collectors.toList());
        log.info("通过ID（从数据库）获取简单商品信息: [{}]",
                JSON.toJSONString(ids));
        List<ScaCommerceGoods> scaCommerceGoods = IterableUtils.toList(
                scaCommerceGoodsDao.findAllById(ids)
        );
        List<SimpleGoodsInfo> result = scaCommerceGoods.stream()
                .map(ScaCommerceGoods::toSimple).collect(Collectors.toList());
        // 将结果缓存, 下一次可以直接从 redis cache 中查询
        log.info("缓存商品信息: [{}]", JSON.toJSONString(ids));

        Map<String, String> id2JsonObject = new HashMap<>(result.size());
        result.forEach(g -> id2JsonObject.put(
                g.getId().toString(), JSON.toJSONString(g)
        ));
        // 保存到 Redis 中
        redisTemplate.opsForHash().putAll(
                GoodsConstant.SCACOMMERCE_GOODS_DICT_KEY, id2JsonObject);
        return result;
    }

    @Override
    public Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {

        // 检验下参数是否合法
        deductGoodsInventories.forEach(d -> {
            if (d.getCount() <= 0) {
                throw new RuntimeException("purchase goods count need > 0");
            }
        });

        List<ScaCommerceGoods> scaCommerceGoods = IterableUtils.toList(
                scaCommerceGoodsDao.findAllById(
                        deductGoodsInventories.stream()
//                                .map(DeductGoodsInventory::getGoodsId)
                                .map(e -> e.getGoodsId())
                                .collect(Collectors.toList())
                )
        );
        // 根据传递的 goodsIds 查询不到商品对象, 抛异常
        if (CollectionUtils.isEmpty(scaCommerceGoods)) {
            throw new RuntimeException("未按要求找到任何商品");
        }
        // 查询出来的商品数量与传递的不一致, 抛异常
        if (scaCommerceGoods.size() != deductGoodsInventories.size()) {
            throw new RuntimeException("请求无效");
        }
        // goodsId -> DeductGoodsInventory
        Map<Long, DeductGoodsInventory> goodsId2Inventory =
                deductGoodsInventories.stream().collect(
                        // 使用Stream时，要将它转换成其他容器或Map。这时候，就会使用到Function.identity()
                        Collectors.toMap(DeductGoodsInventory::getGoodsId, Function.identity())
                );

        // 检查是不是可以扣减库存, 再去扣减库存
        scaCommerceGoods.forEach(g -> {
            // 当前库存
            Long currentInventory = g.getInventory();
            // 需要扣除存货
            Integer needDeductInventory = goodsId2Inventory.get(g.getId()).getCount();
            if (currentInventory < needDeductInventory) {
                log.error("商品库存不足: [{}], [{}]", currentInventory, needDeductInventory);
                throw new RuntimeException("商品库存不足: " + g.getId());
            }
            // 扣减库存
            g.setInventory(currentInventory - needDeductInventory);
            log.info("扣除存货: [{}], [{}], [{}]", g.getId(), currentInventory, g.getInventory());
        });

        scaCommerceGoodsDao.saveAll(scaCommerceGoods);
        log.info("扣除已完成的商品库存");

        return true;
    }
}
