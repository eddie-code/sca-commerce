package com.edcode.commerce.service;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.account.BalanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用于余额相关服务测试
 */
@Slf4j
public class BalanceServiceTest extends BaseTest {

	@Autowired
	private IBalanceService balanceService;

	/**
	 * 测试获取当前用户的余额信息
	 */
	@Test
	public void testGetCurrentUserBalanceInfo() {

		log.info("测试获取当前用户余额信息: [{}]",
				JSON.toJSONString(balanceService.getCurrentUserBalanceInfo()));
	}

	/**
	 * 测试扣减用于余额
	 */
	@Test
	public void testDeductBalance() {

		BalanceInfo balanceInfo = new BalanceInfo();
		balanceInfo.setUserId(loginUserInfo.getId());
            balanceInfo.setBalance(1000L);

		log.info("测试扣除余额: [{}]", JSON.toJSONString(balanceService.deductBalance(balanceInfo)));
	}
}
