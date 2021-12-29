package com.edcode.commerce;

import com.edcode.commerce.conf.DataSourceProxyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Eddie.Lee
 */
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
@Import(DataSourceProxyAutoConfiguration.class)
public class LogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class, args);
    }

}
