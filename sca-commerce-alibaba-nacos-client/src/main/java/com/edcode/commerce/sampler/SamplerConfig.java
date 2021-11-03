package com.edcode.commerce.sampler;

import brave.sampler.RateLimitingSampler;
import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用配置的方式设定抽样率 （二选一）
 */
//@Configuration
public class SamplerConfig {

    /**
     * 限速采集（推荐）
     */
    @Bean
    public Sampler sampler() {
        return RateLimitingSampler.create(100);
    }

//    /**
//     * 概率采集, 默认的采样策略, 默认值是 0.1
//     */
//    @Bean
//    public Sampler defaultSampler() {
//        return ProbabilityBasedSampler.create(0.5f);
//    }
}
