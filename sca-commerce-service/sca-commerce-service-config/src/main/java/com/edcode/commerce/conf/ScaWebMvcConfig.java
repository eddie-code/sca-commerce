package com.edcode.commerce.conf;

import com.edcode.commerce.filter.LoginUserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

    /**
     * 让 MVC 加载 Swagger 的静态资源
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").
                addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

}