package com.edcode.commerce.service.communication.ribbon;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用 Ribbon 之前的配置, 增强 RestTemplate
 */
@Component
public class RibbonConfig {

    /**
     * 注入 RestTemplate
     * @return
     */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
