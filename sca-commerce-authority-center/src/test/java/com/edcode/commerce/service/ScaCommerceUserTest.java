package com.edcode.commerce.service;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.edcode.commerce.dao.ScacommerceUserDao;
import com.edcode.commerce.entity.ScaCommerceUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ScaCommerceUserTest {

	@Autowired
	private ScacommerceUserDao ecommerceUserDao;

	@Test
	public void createUserRecord() {
		ScaCommerceUser ecommerceUser = new ScaCommerceUser();
		ecommerceUser.setUsername("eddie@qq.com");
		ecommerceUser.setPassword(MD5.create().digestHex("12345678"));
		ecommerceUser.setExtraInfo("{}");
		log.info("save user: [{}]", JSON.toJSON(ecommerceUserDao.save(ecommerceUser)));
	}

}
