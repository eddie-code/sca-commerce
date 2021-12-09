package com.edcode.commerce.service.communication.hystrix;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.service.communication.feign.AuthorityFeignClient;
import com.edcode.commerce.vo.JwtToken;
import com.edcode.commerce.vo.UsernameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description AuthorityFeignClient 后备 fallback
 */
@Slf4j
@Component
public class AuthorityFeignClientFallback implements AuthorityFeignClient {

    @Override
    public JwtToken getTokenByFeign(UsernameAndPassword usernameAndPassword) {
        log.info("授权外部客户端通过外部请求获取令牌错误 " + "(Hystrix Fallback)：[{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken("JwtToken Hystrix Fallback");
    }

}
