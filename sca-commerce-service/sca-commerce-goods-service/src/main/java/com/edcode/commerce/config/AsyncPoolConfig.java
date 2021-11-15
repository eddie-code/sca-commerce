package com.edcode.commerce.config;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 自定义异步任务线程池, 异步任务异常捕获处理器
 */
@Slf4j
@EnableAsync    // 开启 Spring 异步任务支持
@Configuration
@RequiredArgsConstructor
public class AsyncPoolConfig implements AsyncConfigurer {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 将自定义的线程池注入到 Spring 容器中
     * */
    @Bean
    @Override
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数
        executor.setCorePoolSize(10);
        // 最大线程数
        executor.setMaxPoolSize(20);
        // 阻塞队列容量
        executor.setQueueCapacity(20);
        // 线程存活时间
        executor.setKeepAliveSeconds(60);
        // 这个非常重要，给线程起前缀的名称
        executor.setThreadNamePrefix("Qinyi-Async-");
        // 等待所有任务结果候再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池任务的等待时间，如果超过设置的时间，就会强制销毁线程池
        executor.setAwaitTerminationSeconds(60);
        // 定义拒绝策略
        executor.setRejectedExecutionHandler(
                // MaxPoolSize + QueueCapacity 满了， 就会执行拒绝策略
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        // 初始化线程池, 初始化 core 线程
        executor.initialize();

        return executor;
    }

    /**
     * 指定系统中的异步任务在出现异常时使用到的处理器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    /**
     * 异步任务异常捕获处理器
     */
    @SuppressWarnings("all")
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {

            throwable.printStackTrace();

            log.error("Async Error: [{}], Method: [{}], Param: [{}]",
                    throwable.getMessage(),
                    method.getName(),
                    JSON.toJSONString(objects));

            // 发送邮件或者是短信, 做进一步的报警处理
            // 建立邮箱消息
            SimpleMailMessage message = new SimpleMailMessage();
            // 发送者
            message.setFrom(username);
            // 接收者
            message.setTo(username);
            // 发送标题
            message.setSubject("Async Error");
            // 发送内容
            message.setText("Async Error: " + throwable.getMessage() + " Method: " + method.getName() + " Param：" + JSON.toJSONString(objects));
            // go
            javaMailSender.send(message);
        }
    }
}
