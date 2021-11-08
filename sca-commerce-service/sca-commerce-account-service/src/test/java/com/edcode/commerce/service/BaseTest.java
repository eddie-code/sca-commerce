package com.edcode.commerce.service;

import com.edcode.commerce.filter.AccessContext;
import com.edcode.commerce.vo.LoginUserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 测试用例基类, 填充登录用户信息
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseTest {

    protected final LoginUserInfo loginUserInfo = new LoginUserInfo(10L,"eddie_k2@qq.com");

    @Before
    public void init() {
        AccessContext.setLoginUserInfo(loginUserInfo);
    }

    @After
    public void destroy() {
        AccessContext.clearLoginUserInfo();
    }

}
