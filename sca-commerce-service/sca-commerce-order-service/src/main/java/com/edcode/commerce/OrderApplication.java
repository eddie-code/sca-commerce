package com.edcode.commerce;

import com.edcode.commerce.conf.DataSourceProxyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 订单微服务启动入口
 */
@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
@Import(DataSourceProxyAutoConfiguration.class) // Seata 使用的数据源
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}
