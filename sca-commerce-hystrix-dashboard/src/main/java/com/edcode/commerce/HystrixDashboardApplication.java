package com.edcode.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description hystrix dashboard 入口
 *
 *  127.0.0.1:9999/scacommerce-hystrix-dashboard/hystrix/
 *  http://127.0.0.1:8000/scacommerce-nacos-client/actuator/hystrix.stream
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableHystrixDashboard     // 开启 Hystrix Dashboard
public class HystrixDashboardApplication {

    public static void main(String[] args) {

        SpringApplication.run(HystrixDashboardApplication.class, args);
    }
}
