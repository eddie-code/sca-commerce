package com.edcode.commerce.service;

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
public class NacosClientService {

    private final DiscoveryClient discoveryClient;

    public NacosClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * 打印 Nacos Client 信息到日志
     * @param serviceId
     * @return
     */
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        log.info("request nacos client to get service instance info: [{}]", serviceId);
        return discoveryClient.getInstances(serviceId);
    }

}
