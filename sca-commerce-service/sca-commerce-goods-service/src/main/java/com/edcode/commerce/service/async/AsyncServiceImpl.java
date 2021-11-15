package com.edcode.commerce.service.async;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.constant.GoodsConstant;
import com.edcode.commerce.dao.ScaCommerceGoodsDao;
import com.edcode.commerce.entity.ScaCommerceGoods;
import com.edcode.commerce.goods.GoodsInfo;
import com.edcode.commerce.goods.SimpleGoodsInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 异步服务接口实现
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AsyncServiceImpl implements  IAsyncService{

    private final ScaCommerceGoodsDao scaCommerceGoodsDao;

    private final StringRedisTemplate redisTemplate;

    /**
     * 异步任务需要加上注解, 并指定使用的线程池
     * 异步任务处理两件事:
     *  1. 将商品信息保存到数据表
     *  2. 更新商品缓存
     * */
    @Async("getAsyncExecutor")
    @Override
    public void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId) {

        log.info("运行taskId的异步任务: [{}]", taskId);

        // StopWatch可以方便记录运行时间，主要用于单线程，单位为ms级，常用于日志记录运行时间
        // https://blog.csdn.net/weixin_45910779/article/details/119035003?spm=1001.2101.3001.6650.16&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-16.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-16.no_search_link
        StopWatch watch = StopWatch.createStarted();

        // 1. 如果是 goodsInfo 中存在重复的商品, 不保存; 直接返回, 记录错误日志
        // 请求数据是否合法的标记
        boolean isIllegal = false;

        // 将商品信息字段 joint 在一起, 用来判断是否存在重复
        Set<String> goodsJointInfos = new HashSet<>(goodsInfos.size());
        // 过滤出来的, 可以入库的商品信息(规则按照自己的业务需求自定义即可)
        List<GoodsInfo> filteredGoodsInfo = new ArrayList<>(goodsInfos.size());

        // 走一遍循环, 过滤非法参数与判定当前请求是否合法
        for (GoodsInfo goods : goodsInfos) {

            // 基本条件不满足的, 直接过滤器
            if (goods.getPrice() <= 0 || goods.getSupply() <= 0) {
                log.info("商品信息无效: [{}]", JSON.toJSONString(goods));
                continue;
            }

            // 组合商品信息（唯一性索引）
            String jointInfo = String.format(
                    "%s,%s,%s",
                    goods.getGoodsCategory(),
                    goods.getBrandCategory(),
                    goods.getGoodsName()
            );

            // 是否包含商品信息
            if (goodsJointInfos.contains(jointInfo)) {
                isIllegal = true;
            }

            // 加入到两个容器中
            goodsJointInfos.add(jointInfo);
            filteredGoodsInfo.add(goods);
        }

        // 如果存在重复商品或者是没有需要入库的商品, 直接打印日志返回
        if (isIllegal || CollectionUtils.isEmpty(filteredGoodsInfo)) {
            watch.stop();
            log.warn("重复商品: [{}]", JSON.toJSONString(filteredGoodsInfo));
            log.info("检查和入库的商品完成: [{}ms]", watch.getTime(TimeUnit.MILLISECONDS));
            return;
        }

        List<ScaCommerceGoods> scaCommerceGoods = filteredGoodsInfo.stream()
                .map(ScaCommerceGoods::to)
                .collect(Collectors.toList());

        List<ScaCommerceGoods> targetGoods = new ArrayList<>(scaCommerceGoods.size());

        // 2. 保存 goodsInfo 之前先判断下是否存在重复商品
        scaCommerceGoods.forEach(g -> {
            // limit 1
            if (null != scaCommerceGoodsDao.findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(
                            g.getGoodsCategory(),
                            g.getBrandCategory(),
                            g.getGoodsName()
                    ).orElse(null)) {
                // 查询到重覆商品直接过滤掉
                return;
            }
            targetGoods.add(g);
        });

        // 商品信息入库
        List<ScaCommerceGoods> savedGoods = IterableUtils.toList(
                // 返回的是 Iterable 的迭代，所以需要 toList一次
                scaCommerceGoodsDao.saveAll(targetGoods)
        );
        // TODO 将入库商品信息同步到 Redis 中
        saveNewGoodsInfoToRedis(savedGoods);

        log.info("将商品信息保存到db和redis: [{}]", savedGoods.size());

        watch.stop();
        log.info("检查和入库商品完成: [{}ms]", watch.getTime(TimeUnit.MILLISECONDS));
    }

    /**
     * 将保存到数据表中的数据缓存到 Redis 中
     * dict: key -> <id, SimpleGoodsInfo(json)>
     */
    private void saveNewGoodsInfoToRedis(List<ScaCommerceGoods> savedGoods) {

        // 由于 Redis 是内存存储, 只存储简单商品信息
        List<SimpleGoodsInfo> simpleGoodsInfos = savedGoods.stream()
//                .map(e -> e.toSimple())
                .map(ScaCommerceGoods::toSimple)
                .collect(Collectors.toList());

        Map<String, String> id2JsonObject = new HashMap<>(simpleGoodsInfos.size());
        simpleGoodsInfos.forEach(
                g -> id2JsonObject.put(g.getId().toString(), JSON.toJSONString(g))
        );

        // 保存到 Redis 中
        redisTemplate.opsForHash().putAll(
                GoodsConstant.SCACOMMERCE_GOODS_DICT_KEY,
                id2JsonObject
        );
    }

}
