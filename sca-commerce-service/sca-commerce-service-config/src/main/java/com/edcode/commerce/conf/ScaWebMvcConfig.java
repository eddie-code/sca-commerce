package com.edcode.commerce.conf;

import com.edcode.commerce.filter.LoginUserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description Web Mvc 配置
 */
@Configuration
public class ScaWebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 添加拦截器配置
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 添加【用户身份统一登录拦截】
        registry.addInterceptor(new LoginUserInfoInterceptor()).addPathPatterns("/**").order(0);
    }

}