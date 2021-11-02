package com.edcode.commerce.filter.factory;

import com.edcode.commerce.filter.HeaderTokenGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 工厂模式
 *
 *     "filters": [
 *       {
 *         "name": "HeaderToken"
 *       },
 *       ...
 *
 *   "name": "HeaderToken" 其实就是 HeaderTokenGatewayFilterFactory 只是gateway 会过滤掉 GatewayFilterFactory 只取 HeaderToken
 */
@Component
public class HeaderTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return new HeaderTokenGatewayFilter();
    }

}
