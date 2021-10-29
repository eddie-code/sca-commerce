package com.edcode.commerce.controller;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.annotation.IgnoreResponseAdvice;
import com.edcode.commerce.service.JwtService;
import com.edcode.commerce.vo.JwtToken;
import com.edcode.commerce.vo.UsernameAndPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 对外暴露的授权服务接口
 */
@Slf4j
@RestController
@RequestMapping("/authority")
@RequiredArgsConstructor
public class AuthorityController {

	private final JwtService jwtService;

	/**
	 * 从授权中心获取 Token (其实就是登录功能), 且返回信息中没有统一响应的包装
	 */
	@IgnoreResponseAdvice
	@PostMapping("/token")
	public JwtToken token(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
		log.info("request to get token with param: [{}]", JSON.toJSONString(usernameAndPassword));
		return new JwtToken(
				jwtService.generateToken(
				        usernameAndPassword.getUsername(), usernameAndPassword.getPassword()
                )
        );
	}

	/**
	 * 注册用户并返回当前注册用户的 Token, 即通过授权中心创建用户
	 */
	@IgnoreResponseAdvice
	@PostMapping("/register")
	public JwtToken register(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
		log.info("register user with param: [{}]", JSON.toJSONString(usernameAndPassword));
		return new JwtToken(
				jwtService.registerUserAndGenerateToken(usernameAndPassword)
		);
	}
}
