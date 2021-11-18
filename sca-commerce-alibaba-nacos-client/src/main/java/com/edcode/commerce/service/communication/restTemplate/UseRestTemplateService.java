package com.edcode.commerce.service.communication.restTemplate;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.constant.CommonConstant;
import com.edcode.commerce.vo.JwtToken;
import com.edcode.commerce.vo.UsernameAndPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用 RestTemplate 实现微服务通信
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UseRestTemplateService {

	/**
	 * 随机挑选客户端（负载均衡）
	 */
	private final LoadBalancerClient loadBalancerClient;

	/**
	 * 从授权服务中获取 JwtToken
	 */
	public JwtToken getTokenFromAuthorityService(UsernameAndPassword usernameAndPassword) {

		// 第一种方式: 写死 url
		String requestUrl = "http://127.0.0.1:7000/scacommerce-authority-center" + "/authority/token";
		log.info("RestTemplate请求url和正文: [{}], [{}]",
				requestUrl,
				JSON.toJSONString(usernameAndPassword)
		);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new RestTemplate().postForObject(
				requestUrl,
				new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers),
				JwtToken.class
		);
	}

	/**
	 * 从授权服务中获取 JwtToken, 且带有负载均衡
	 */
	public JwtToken getTokenFromAuthorityServiceWithLoadBalancer(UsernameAndPassword usernameAndPassword) {

		// 第二种方式: 通过注册中心拿到服务的信息(是所有的实例), 再去发起调用
		ServiceInstance serviceInstance = loadBalancerClient.choose(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);
		log.info("Nacos客户端信息: [{}], [{}], [{}]",
				serviceInstance.getServiceId(),
				serviceInstance.getInstanceId(),
				JSON.toJSONString(serviceInstance.getMetadata())
		);

		// 与第一种方式区别，就是不用在写死 url, 从 loadBalancerClient 里面获取
		String requestUrl = String.format(
				"http://%s:%s/scacommerce-authority-center/authority/token",
				serviceInstance.getHost(),
				serviceInstance.getPort()
		);
		log.info("登录请求url和正文: [{}], [{}]", requestUrl, JSON.toJSONString(usernameAndPassword));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new RestTemplate().postForObject(
				requestUrl,
				new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers),
				JwtToken.class
		);
	}
}