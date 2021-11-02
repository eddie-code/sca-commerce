package com.edcode.commerce.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 数据配置绑定
 */
@Data
@Component
@ConfigurationProperties(prefix = "project")
public class ProjectConfig {

    private String name;
    private String org;
    private String version;
    private String author;
}
