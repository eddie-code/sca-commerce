package com.edcode.commerce.service.communication.hystrix;

import com.edcode.commerce.service.communication.feign.AuthorityFeignClient;
import com.edcode.commerce.vo.JwtToken;
import com.edcode.commerce.vo.UsernameAndPassword;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description OpenFeign 集成 Hystrix 的另外一种模式
 */
@Slf4j
@Component
public class AuthorityFeignClientFallbackFactory implements FallbackFactory<AuthorityFeignClient> {

    @Override
    public AuthorityFeignClient create(Throwable throwable) {

        log.warn("授权外部客户端通过外部请求获取令牌错误 " + "(Hystrix FallbackFactory)：[{}]", throwable.getMessage(),throwable);

        return new AuthorityFeignClient(){

            @Override
            public JwtToken getTokenByFeign(UsernameAndPassword usernameAndPassword) {
                return new JwtToken("JwtToken Hystrix FallbackFactory");
            }
        };
    }

}
