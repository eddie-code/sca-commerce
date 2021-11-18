package com.edcode.commerce.controller;

import com.edcode.commerce.service.communication.feign.AuthorityFeignClient;
import com.edcode.commerce.service.communication.feign.UseFeignApi;
import com.edcode.commerce.service.communication.restTemplate.UseRestTemplateService;
import com.edcode.commerce.service.communication.ribbon.UseRibbonService;
import com.edcode.commerce.vo.JwtToken;
import com.edcode.commerce.vo.UsernameAndPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 微服务通信 Controller
 */
@RestController
@RequestMapping("/communication")
@RequiredArgsConstructor
public class CommunicationController {

	private final UseRestTemplateService restTemplateService;
	private final UseRibbonService ribbonService;
	private final AuthorityFeignClient feignClient;
	private final UseFeignApi useFeignApi;

	@PostMapping("/rest-template")
	public JwtToken getTokenFromAuthorityService(@RequestBody UsernameAndPassword usernameAndPassword) {
		return restTemplateService.getTokenFromAuthorityService(usernameAndPassword);
	}

	@PostMapping("/rest-template-load-balancer")
	public JwtToken getTokenFromAuthorityServiceWithLoadBalancer(@RequestBody UsernameAndPassword usernameAndPassword) {
		return restTemplateService.getTokenFromAuthorityServiceWithLoadBalancer(usernameAndPassword);
	}

	@PostMapping("/ribbon")
	public JwtToken getTokenFromAuthorityServiceByRibbon(@RequestBody UsernameAndPassword usernameAndPassword) {
		return ribbonService.getTokenFromAuthorityServiceByRibbon(usernameAndPassword);
	}

	@PostMapping("/thinking-in-ribbon")
	public JwtToken thinkingInRibbon(@RequestBody UsernameAndPassword usernameAndPassword) {
		return ribbonService.thinkingInRibbon(usernameAndPassword);
	}

	@PostMapping("/token-by-feign")
	public JwtToken getTokenByFeign(@RequestBody UsernameAndPassword usernameAndPassword) {
		return feignClient.getTokenByFeign(usernameAndPassword);
	}

	@PostMapping("/thinking-in-feign")
	public JwtToken thinkingInFeign(@RequestBody UsernameAndPassword usernameAndPassword) {
		return useFeignApi.thinkingInFeign(usernameAndPassword);
	}

}
