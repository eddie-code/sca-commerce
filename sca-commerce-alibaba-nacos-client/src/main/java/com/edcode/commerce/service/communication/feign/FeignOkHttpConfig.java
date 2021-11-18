package com.edcode.commerce.service.communication.feign;

import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description OpenFeign 使用 OkHttp 配置类
 *
 *   尽管不写这个配置类，okhttp也是能用，这个配置类并非必要，但通常都会写，使得 okhttp 更友好
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class) // 在 feign 初始化之前就注入 okhttp
public class FeignOkHttpConfig {

    /**
     * 注入 OkHttp, 并自定义配置
     * @return
     */
    @Bean
    public okhttp3.OkHttpClient okHttpClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)    // 设置连接超时
                .readTimeout(5, TimeUnit.SECONDS)   // 设置读超时
                .writeTimeout(5, TimeUnit.SECONDS)  // 设置写超时
                .retryOnConnectionFailure(true)     // 是否自动重连
                // 配置连接池中的最大空闲线程个数为 10, 并保持 5 分钟
                .connectionPool(
                        new ConnectionPool(10, 5L, TimeUnit.MINUTES)
                )
                .build();
    }

}
