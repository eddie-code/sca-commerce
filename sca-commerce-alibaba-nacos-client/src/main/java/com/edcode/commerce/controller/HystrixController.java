package com.edcode.commerce.controller;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.service.NacosClientService;
import com.edcode.commerce.service.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import lombok.RequiredArgsConstructor;
import rx.Observable;
import rx.Observer;

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

    private final CacheHystrixCommandAnnotation cacheHystrixCommandAnnotation;

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

    @GetMapping("/hystrix-observable-command")
    public List<ServiceInstance> getServiceInstancesByServiceIdObservable(
            @RequestParam String serviceId) {

        List<String> serviceIds = Arrays.asList(serviceId, serviceId, serviceId);
        List<List<ServiceInstance>> result = new ArrayList<>(serviceIds.size());

        NacosClientHystrixObservableCommand observableCommand =
                new NacosClientHystrixObservableCommand(nacosClientService, serviceIds);

        // 异步执行命令
        Observable<List<ServiceInstance>> observe = observableCommand.observe();

        // 注册获取结果
        observe.subscribe(
                new Observer<List<ServiceInstance>>() {

                    // 执行 onNext 之后再去执行 onCompleted
                    @Override
                    public void onCompleted() {
                        log.info("all tasks is complete: [{}], [{}]",
                                serviceId, Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<ServiceInstance> instances) {
                        result.add(instances);
                    }
                }
        );

        log.info("observable command result is : [{}], [{}]",
                JSON.toJSONString(result), Thread.currentThread().getName());
        return result.get(0);
    }

    @GetMapping("/cache-hystrix-command")
    public void cacheHystrixCommand(@RequestParam String serviceId) {

        // 使用缓存 Command, 发起两次请求
        CacheHystrixCommand command1 = new CacheHystrixCommand(
                nacosClientService, serviceId
        );
        CacheHystrixCommand command2 = new CacheHystrixCommand(
                nacosClientService, serviceId
        );

        List<ServiceInstance> result01 = command1.execute();
        List<ServiceInstance> result02 = command2.execute();
        log.info("result01, result02: [{}], [{}]",
                JSON.toJSONString(result01), JSON.toJSONString(result02));

        // 清除缓存
        CacheHystrixCommand.flushRequestCache(serviceId);

        // 使用缓存 Command, 发起两次请求
        CacheHystrixCommand command3 = new CacheHystrixCommand(
                nacosClientService, serviceId
        );
        CacheHystrixCommand command4 = new CacheHystrixCommand(
                nacosClientService, serviceId
        );

        List<ServiceInstance> result03 = command3.execute();
        List<ServiceInstance> result04 = command4.execute();
        log.info("result03, result04: [{}], [{}]",
                JSON.toJSONString(result03), JSON.toJSONString(result04));
    }


    @GetMapping("/cache-annotation-01")
    public List<ServiceInstance> useCacheByAnnotation01(@RequestParam String serviceId) {

        log.info("使用 cache by annotation01（控制器）获取nacos客户端信息: [{}]", serviceId);

        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        List<ServiceInstance> result02 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);

        // 清除掉缓存
        cacheHystrixCommandAnnotation.flushCacheByAnnotation01(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        // 这里有第四次调用
        return cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
    }

    @GetMapping("/cache-annotation-02")
    public List<ServiceInstance> useCacheByAnnotation02(@RequestParam String serviceId) {

        log.info("使用 cache by annotation02（控制器）获取nacos客户端信息: [{}]", serviceId);

        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        List<ServiceInstance> result02 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);

        // 清除掉缓存
        cacheHystrixCommandAnnotation.flushCacheByAnnotation02(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        // 这里有第四次调用
        return cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
    }

    @GetMapping("/cache-annotation-03")
    public List<ServiceInstance> useCacheByAnnotation03(@RequestParam String serviceId) {

        log.info("使用 cache by annotation03（控制器）获取nacos客户端信息: [{}]", serviceId);

        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        List<ServiceInstance> result02 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);

        // 清除掉缓存
        cacheHystrixCommandAnnotation.flushCacheByAnnotation03(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        // 这里有第四次调用
        return cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
    }


}
