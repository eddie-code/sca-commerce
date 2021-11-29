package com.edcode.commerce.controller;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.service.NacosClientService;
import com.edcode.commerce.service.hystrix.NacosClientHystrixCommand;
import com.edcode.commerce.service.hystrix.UseHystrixCommandAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Future;

import lombok.RequiredArgsConstructor;
import rx.Observable;

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

    private final NacosClientService nacosClientService;

    @GetMapping("/hystrix-command-annotation")
    public List<ServiceInstance> getNacosClientInfoUseAnnotation(@RequestParam String serviceId) {

        log.info("请求nacos客户端信息使用注解: [{}], [{}]",
                serviceId,
                Thread.currentThread().getName()
        );

        return hystrixCommandAnnotation.getNacosClientInfo(serviceId);
    }

    @GetMapping("/simple-hystrix-command")
    public List<ServiceInstance> getServiceInstanceByServiceId(@RequestParam String serviceId) throws Exception {

        // 第一种方式
        List<ServiceInstance> serviceInstances01 = new NacosClientHystrixCommand(nacosClientService, serviceId).execute();    // 同步阻塞

        log.info("使用execute获取服务实例: [{}], [{}]", JSON.toJSONString(serviceInstances01), Thread.currentThread().getName());

        // 第二种方式
        List<ServiceInstance> serviceInstances02;
        Future<List<ServiceInstance>> future = new NacosClientHystrixCommand(nacosClientService, serviceId).queue();      // 异步非阻塞
        // 这里可以做一些别的事, 需要的时候再去拿结果
        serviceInstances02 = future.get();
        log.info("使用队列获取服务实例: [{}], [{}]", JSON.toJSONString(serviceInstances02), Thread.currentThread().getName());

        // 第三种方式
        Observable<List<ServiceInstance>> observable = new NacosClientHystrixCommand(nacosClientService, serviceId).observe();        // 热响应调用
        List<ServiceInstance> serviceInstances03 = observable.toBlocking().single();
        log.info("使用“观察”获取服务实例: [{}], [{}]", JSON.toJSONString(serviceInstances03), Thread.currentThread().getName());

        // 第四种方式
        Observable<List<ServiceInstance>> toObservable = new NacosClientHystrixCommand(nacosClientService, serviceId).toObservable();        // 异步冷响应调用
        List<ServiceInstance> serviceInstances04 = toObservable.toBlocking().single();
        log.info("使用toObservable获取服务实例: [{}], [{}]", JSON.toJSONString(serviceInstances04), Thread.currentThread().getName());

        // execute = queue + get
        return serviceInstances01;
    }

}
