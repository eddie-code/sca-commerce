package com.edcode.commerce.dao;

import com.edcode.commerce.entity.ScaCommerceAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description ScaCommerceAddress Dao 接口定义
 */
public interface ScaCommerceAddressDao extends JpaRepository<ScaCommerceAddress, Long> {

	/**
	 * 根据 用户 id 查询地址信息
	 */
	List<ScaCommerceAddress> findAllByUserId(Long userId);
}
