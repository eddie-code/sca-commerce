package com.edcode.commerce.service.hystrix;

import com.edcode.commerce.service.NacosClientService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description NacosClientService 实现包装
 *
 * Hystrix 舱壁模式:
 *  1. 线程池
 *  2. 信号量: 算法 + 数据结构, 有限状态机
 *
 */
@Slf4j
public class NacosClientHystrixCommand extends HystrixCommand<List<ServiceInstance>> {

    /** 需要保护的服务 */
    private final NacosClientService nacosClientService;

    /** 方法需要传递的参数 */
    private final String serviceId;

    public NacosClientHystrixCommand(NacosClientService nacosClientService, String serviceId) {

        super(
                Setter.withGroupKey(
                        HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixCommand"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("NacosClientPool"))
                        // 线程池 key 配置
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        // 线程池隔离策略
                                        .withExecutionIsolationStrategy(THREAD)
                                        // 开启降级
                                        .withFallbackEnabled(true)
                                        // 开启熔断器 ： 比如当出现异常时候，就会熔断
                                        .withCircuitBreakerEnabled(true)
                        )
        );

        // 可以配置信号量隔离策略
//        Setter semaphore =
//                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
//                .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixCommand"))
//                .andCommandPropertiesDefaults(
//                        HystrixCommandProperties.Setter()
//                        .withCircuitBreakerRequestVolumeThreshold(10)
//                        .withCircuitBreakerSleepWindowInMilliseconds(5000)
//                        .withCircuitBreakerErrorThresholdPercentage(50)
//                        .withExecutionIsolationStrategy(SEMAPHORE)  // 指定使用信号量隔离
//                        //.....
//                );

        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    /**
     * 要保护的方法调用写在 run 方法中
     * */
    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("使用Hystrix命令中的 NacosClientService 获取服务实例: [{}], [{}]", this.serviceId, Thread.currentThread().getName());
        return this.nacosClientService.getNacosClientInfo(this.serviceId);
    }

    /**
     * 降级处理策略
     * */
    @Override
    protected List<ServiceInstance> getFallback() {
        log.warn("NacosClientService 运行错误: [{}], [{}]", this.serviceId, Thread.currentThread().getName());
        return Collections.emptyList();
    }
}
