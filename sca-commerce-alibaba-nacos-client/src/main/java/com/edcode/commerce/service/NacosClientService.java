package com.edcode.commerce.service;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
//        throw new RuntimeException("has some error");

        log.info("请求nacos客户端获取服务实例信息: [{}]", serviceId);
        return discoveryClient.getInstances(serviceId);
    }

    /**
     * 提供给编程方式的 Hystrix 请求合并
     */
    public List<List<ServiceInstance>> getNacosClientInfos(List<String> serviceIds) {

        log.info("请求nacos客户端获取服务实例信息: [{}]", JSON.toJSONString(serviceIds));
        List<List<ServiceInstance>> result = new ArrayList<>(serviceIds.size());

        serviceIds.forEach(s -> result.add(discoveryClient.getInstances(s)));
        return result;
    }

}
