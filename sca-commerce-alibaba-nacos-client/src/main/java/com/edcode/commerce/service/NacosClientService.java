package com.edcode.commerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NacosClientService {

    private final DiscoveryClient discoveryClient;

    /**
     * 打印 Nacos Client 信息到日志
     * @param serviceId
     * @return
     */
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {

        // UseHystrixCommandAnnotation 测试超时
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            //
//        }

        // NacosClientHystrixCommand 测试熔断
        throw new RuntimeException("has some error");

//        log.info("请求nacos客户端获取服务实例信息: [{}]", serviceId);
//        return discoveryClient.getInstances(serviceId);
    }

}
