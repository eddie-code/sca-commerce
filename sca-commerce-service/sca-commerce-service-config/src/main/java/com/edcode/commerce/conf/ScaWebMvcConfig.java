package com.edcode.commerce.conf;

import com.alibaba.cloud.seata.web.SeataHandlerInterceptor;
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
        // Seata 传递 xid 事务 id 给其他的微服务
        // 只有这样, 其他的服务才会写 undo_log, 才能够实现回滚
        registry.addInterceptor(new SeataHandlerInterceptor()).addPathPatterns("/**");
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