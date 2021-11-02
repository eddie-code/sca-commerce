package com.edcode.commerce.conf;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.web.client.RestTemplate;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 网关需要注入到容器中的 Bean
 */
@Configuration
public class GatewayBeanConf {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
