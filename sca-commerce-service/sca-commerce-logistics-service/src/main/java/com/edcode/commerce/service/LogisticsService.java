package com.edcode.commerce.service;

import org.springframework.messaging.handler.annotation.Payload;

/**
 * @author eddie.lee
 * @description
 */
public interface LogisticsService {

    /**
     * 订阅监听订单微服务发送的物流消息
     * @param payload
     */
    void consumeLogisticsMessage(@Payload Object payload);

}
