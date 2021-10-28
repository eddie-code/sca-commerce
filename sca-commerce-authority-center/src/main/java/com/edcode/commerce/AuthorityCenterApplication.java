package com.edcode.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 授权中心启动入口
 */
@EnableJpaAuditing  // 允许 Jpa 自动审计
@EnableDiscoveryClient
@SpringBootApplication
public class AuthorityCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorityCenterApplication.class, args);
    }

}
