package com.edcode.commerce.service;

import com.edcode.commerce.account.BalanceInfo;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用于余额相关的服务接口定义
 */
public interface IBalanceService {

	/**
	 * 获取当前用户余额信息
	 */
	BalanceInfo getCurrentUserBalanceInfo();

	/**
	 * >扣减用户余额
	 * 
	 * @param balanceInfo
	 *            代表想要扣减的余额
	 */
	BalanceInfo deductBalance(BalanceInfo balanceInfo);

}
