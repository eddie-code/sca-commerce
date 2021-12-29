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
 * @description 用户账户微服务启动入口
 *
 * localhost:8003/scacommerce-account-service/doc.html
 * localhost:8003/scacommerce-account-service/swagger-ui.html
 */
@EnableJpaAuditing // 审计功能
@SpringBootApplication
@EnableDiscoveryClient
@Import(DataSourceProxyAutoConfiguration.class) // Seata 使用的数据源
public class AccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}
}