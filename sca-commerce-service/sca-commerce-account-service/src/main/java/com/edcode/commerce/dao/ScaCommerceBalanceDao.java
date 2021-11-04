package com.edcode.commerce.dao;

import com.edcode.commerce.entity.ScaCommerceBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description ScaCommerceBalance Dao 接口定义
 */
public interface ScaCommerceBalanceDao extends JpaRepository<ScaCommerceBalance, Long> {

	/**
	 * 根据 userId 查询 ScaCommerceBalance 对象
	 */
	ScaCommerceBalance findByUserId(Long userId);

}
