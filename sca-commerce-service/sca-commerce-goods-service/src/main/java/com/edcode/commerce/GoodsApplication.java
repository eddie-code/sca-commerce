package com.edcode.commerce;

import com.edcode.commerce.conf.DataSourceProxyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 商品微服务启动入口
 *
 * 启动依赖组件/中间件: Redis + MySQL + Nacos + Kafka + Zipkin
 *
 * http://127.0.0.1:8001/scacommerce-goods-service/doc.html
 *
 */
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
@Import(DataSourceProxyAutoConfiguration.class) // Seata 使用的数据源
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}

