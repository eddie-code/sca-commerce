package com.edcode.commerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.dao.ScaCommerceLogisticsDao;
import com.edcode.commerce.entity.ScaCommerceLogistics;
import com.edcode.commerce.order.LogisticsMessage;
import com.edcode.commerce.service.LogisticsService;
import com.edcode.commerce.sink.LogisticsSink;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * @author eddie.lee
 * @description 物流服务实现
 */
@Slf4j
@EnableBinding(LogisticsSink.class)
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {

    private final ScaCommerceLogisticsDao logisticsDao;

    @Override
    @StreamListener("logisticsInput")
    public void consumeLogisticsMessage(@Payload Object payload) {
        log.info("接收和消费物流信息: [{}]", payload.toString());
        LogisticsMessage logisticsMessage = JSON.parseObject(
                payload.toString(),
                LogisticsMessage.class
        );
        String extraInfo = logisticsMessage.getExtraInfo();
        ScaCommerceLogistics ecommerceLogistics = logisticsDao.save(
                ScaCommerceLogistics.builder()
                        .userId(logisticsMessage.getUserId())
                        .orderId(logisticsMessage.getOrderId())
                        .addressId(logisticsMessage.getAddressId())
                        .extraInfo(StringUtils.isNotBlank(extraInfo) ? extraInfo : "{}")
                        .build()
        );
        log.info("消费物流信息成功: [{}]", ecommerceLogistics.getId());
    }
}