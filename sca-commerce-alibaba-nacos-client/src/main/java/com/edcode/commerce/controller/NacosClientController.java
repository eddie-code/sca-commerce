package com.edcode.commerce.controller;

import com.edcode.commerce.service.NacosClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/nacos-client")
@RequiredArgsConstructor
public class NacosClientController {

    private final NacosClientService nacosClientService;

    /**
     * 根据 service id 获取服务所有的实例信息
     */
    @GetMapping("/service-instance")
    public List<ServiceInstance> logNacosClientInfo(@RequestParam(defaultValue = "sca-commerce-nacos-client") String serviceId) {
        log.info("coming in log nacos client info: [{}]", serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }


}
