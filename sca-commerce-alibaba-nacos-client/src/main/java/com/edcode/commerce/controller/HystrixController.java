package com.edcode.commerce.controller;

import com.edcode.commerce.service.hystrix.UseHystrixCommandAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/hystrix")
@RequiredArgsConstructor
public class HystrixController {

	private final UseHystrixCommandAnnotation hystrixCommandAnnotation;

    @GetMapping("/hystrix-command-annotation")
    public List<ServiceInstance> getNacosClientInfoUseAnnotation(@RequestParam String serviceId) {

        log.info("请求nacos客户端信息使用注解: [{}], [{}]",
                serviceId,
                Thread.currentThread().getName()
        );

        return hystrixCommandAnnotation.getNacosClientInfo(serviceId);
    }

}
