package com.edcode.commerce.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description HTTP 请求头部携带 Token 验证过滤器
 */
public class HeaderTokenGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 从 HTTP Header 中寻找 key 为 token, value 为 imooc 的键值对
        String name = exchange.getRequest().getHeaders().getFirst("token");
        if ("edcode".equals(name)) {
            return chain.filter(exchange);
        }

        // 标记此次请求没有权限, 并结束这次请求, 返回401
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        // 数字越少，执行越优先
        return HIGHEST_PRECEDENCE + 2;
    }
}
