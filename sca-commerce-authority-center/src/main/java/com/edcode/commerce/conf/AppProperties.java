package com.edcode.commerce.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 读取 bootstrap.yml 或 application.yml 属性
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = "sca-commerce")
public class AppProperties {

    /**
     * 授权需要使用的一些常量信息
     */
    @Getter
    @Setter
    public static class Rsa{

        /**
         * RSA 私钥, 除了授权中心以外, 不暴露给任何客户端
         *
         * RSA 公钥。不应该显示在这里，应该 sca-commerce-common 读取
         */
        private String privateKey;

        /**
         * 默认的 Token 超时时间, 一天
         */
        private Integer defaultExpireDay = 1;
    }

    @Getter
    @Setter
    private Rsa rsa = new Rsa();

}
