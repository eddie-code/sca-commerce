package com.edcode.commerce.service.communication.feign;

import com.edcode.commerce.vo.JwtToken;
import com.edcode.commerce.vo.UsernameAndPassword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 与 Authority 服务通信的 Feign Client 接口定义
 */
@FeignClient(contextId = "AuthorityFeignClient", value = "sca-commerce-authority-center")
public interface AuthorityFeignClient {

    /**
     * 通过 OpenFeign 访问 Authority 获取 Token
     * @param usernameAndPassword
     * @return
     */
	@RequestMapping(value = "/scacommerce-authority-center/authority/token", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	JwtToken getTokenByFeign(@RequestBody UsernameAndPassword usernameAndPassword);

}
