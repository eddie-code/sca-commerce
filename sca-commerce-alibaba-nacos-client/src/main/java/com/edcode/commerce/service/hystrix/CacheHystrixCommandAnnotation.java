package com.edcode.commerce.service.hystrix;

import com.edcode.commerce.service.NacosClientService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用注解方式开启 Hystrix 请求缓存
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheHystrixCommandAnnotation {

    private final NacosClientService nacosClientService;


    // 第一种 Hystrix Cache 注解的使用方法 （通常很少会这样写）
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation01(String serviceId) {
        log.info("使用 cache01 获取nacos客户端信息: [{}]", serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }

    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation",
            cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public void flushCacheByAnnotation01(String cacheId) {
        log.info("刷新hystrix缓存密钥: [{}]", cacheId);
    }

    public String getCacheKey(String cacheId) {
        return cacheId;
    }

    // 第二种 Hystrix Cache 注解的使用方法 （推荐：上面的升级版，可以直观简单的知道 CacheKey）
    @CacheResult
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation02(@CacheKey String serviceId) {

        log.info("使用 cache02 获取nacos客户端信息: [{}]", serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }

    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation")
    @HystrixCommand
    public void flushCacheByAnnotation02(@CacheKey String cacheId) {
        log.info("刷新hystrix缓存密钥: [{}]", cacheId);
    }

    // 第三种 Hystrix Cache 注解的使用方法 （不推荐使用）
    @CacheResult
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation03(String serviceId) {

        log.info("使用 cache03 获取nacos客户端信息: [{}]", serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }

    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation")
    @HystrixCommand
    public void flushCacheByAnnotation03(String cacheId) {
        log.info("刷新hystrix缓存密钥: [{}]", cacheId);
    }

}
