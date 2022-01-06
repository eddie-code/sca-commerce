package com.edcode.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author eddie.lee
 * @description Sentinel 集成
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SentinelClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelClientApplication.class, args);
    }

}
