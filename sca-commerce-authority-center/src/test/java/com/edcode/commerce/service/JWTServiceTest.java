package com.edcode.commerce.service;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.util.TokenParseUtil;
import com.edcode.commerce.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description JWT 相关服务测试类
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class JWTServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    public void testGenerateAndParseToken() throws Exception {

        // 根据用户名和密码查询，然后把 id, username 封装成 jwt payload
        String jwtToken = jwtService.generateToken(
                "eddie@qq.com",
                "25d55ad283aa400af464c76d713c07ad"
        );

        // 根据用户名密码生成出来的 JWT Token
        log.info("JWT Token Is: [{}]", jwtToken);

        // JWT Token中解析 LoginUserInfo 对象
        LoginUserInfo userInfo = TokenParseUtil.parseUserInfoFromToken(jwtToken);
        log.info("解析令牌: [{}]", JSON.toJSONString(userInfo));
    }
}