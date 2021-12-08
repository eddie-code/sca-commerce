package com.edcode.commerce.service.hystrix.request_merge;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.service.NacosClientService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 批量请求 Hystrix Command
 */
@Slf4j
public class NacosClientBatchCommand extends HystrixCommand<List<List<ServiceInstance>>> {

    private final NacosClientService nacosClientService;

    private final List<String> serviceIds;

    protected NacosClientBatchCommand(NacosClientService nacosClientService, List<String> serviceIds) {

        super(
                HystrixCommand.Setter.withGroupKey(
                        HystrixCommandGroupKey.Factory.asKey("NacosClientBatchCommand")
                )
        );

        this.nacosClientService = nacosClientService;
        this.serviceIds = serviceIds;
    }

    @Override
    protected List<List<ServiceInstance>> run() throws Exception {
        log.info("使用nacos客户端批处理命令获取结果: [{}]", JSON.toJSONString(serviceIds));
        return nacosClientService.getNacosClientInfos(serviceIds);
    }

    @Override
    protected List<List<ServiceInstance>> getFallback() {
        log.warn("nacos客户端批处理命令失败，请使用回退！");
        return Collections.emptyList();
    }
}