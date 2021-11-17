package com.edcode.commerce.service;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.common.TableId;
import com.edcode.commerce.goods.DeductGoodsInventory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 商品微服务功能测试
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsServiceTest {

    @Autowired
    private IGoodsService goodsService;

    @Test
    public void testGetGoodsInfoByTableId() {

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<TableId.Id> tIds = ids.stream()
                .map(TableId.Id::new)
                .collect(Collectors.toList());

        List<TableId.Id> tIds2 = new ArrayList<>();
        for (Long id : ids) {
            TableId.Id tid1 = new TableId.Id();
            tid1.setId(id);
            tIds2.add(tid1);
        }

        log.info("JDK8 - 测试按表id获取商品信息: [{}]",
                JSON.toJSONString(
                        goodsService.getGoodsInfoByTableId(new TableId(tIds))
                )
        );

        log.info("JDK7 - 测试按表id获取商品信息: [{}]",
                JSON.toJSONString(
                        goodsService.getGoodsInfoByTableId(new TableId(tIds2))
                )
        );
    }

    @Test
    public void testGetSimpleGoodsInfoByPage() {
        log.info("测试通过页面获取简单商品信息: [{}]", JSON.toJSONString(
                goodsService.getSimpleGoodsInfoByPage(1)
        ));
    }

    @Test
    public void testGetSimpleGoodsInfoByTableId() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<TableId.Id> tIds = ids.stream().map(TableId.Id::new).collect(Collectors.toList());
        log.info("测试通过表id获取简单商品信息: [{}]", JSON.toJSONString(
                goodsService.getSimpleGoodsInfoByTableId(new TableId(tIds))
        ));
    }

    @Test
    public void testDeductGoodsInventory() {
        List<DeductGoodsInventory> deductGoodsInventories = Arrays.asList(
                new DeductGoodsInventory(1L, 100),
                new DeductGoodsInventory(2L, 66)
        );
        log.info("测试扣除商品库存: [{}]",
                goodsService.deductGoodsInventory(deductGoodsInventories)
        );
    }
}