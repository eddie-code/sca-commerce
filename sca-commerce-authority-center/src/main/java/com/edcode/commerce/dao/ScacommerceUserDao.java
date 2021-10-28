package com.edcode.commerce.dao;

import com.edcode.commerce.entity.ScaCommerceUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description EcommerceUser Dao 接口定义
 */
public interface ScacommerceUserDao extends JpaRepository<ScaCommerceUser, Long> {

	/**
	 * 根据用户名查询 EcommerceUser 对象
	 * <p>
	 * select * from t_ecommerce_user where username = ?
	 */
	ScaCommerceUser findByUsername(String username);

	/**
	 * 根据用户名和密码查询实体对象
	 * <p>
	 * </>select * from t_ecommerce_user where username = ? and password = ?
	 */
	ScaCommerceUser findByUsernameAndPassword(String username, String password);

}
