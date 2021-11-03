package com.edcode.commerce.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description Swagger 配置类
 *
 * ip:port/swagger-ui.html
 *
 * ip:port/doc.html
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Swagger 实例 Bean 是 Docket, 所以通过配置 Docket 实例来配置 Swagger
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 展示在 Swagger 页面上的自定义工程描述信息
                .apiInfo(apiInfo())
                // 选择展示哪些接口
                .select()
                // 只有 com.edcode.ecommerce 包内的才去展示
                .apis(RequestHandlerSelectors.basePackage("com.edcode.commerce"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Swagger 的描述信息
     */
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("sca-commerce-service")
                .description("sca-commerce-springcloud-service")
                .contact(new Contact(
                        "eddie", "blog.eddilee.cn", "xxx@qq.com"
                ))
                .version("1.0")
                .build();
    }
}