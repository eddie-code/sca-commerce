package com.edcode.commerce.service;

import com.edcode.commerce.vo.UsernameAndPassword;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description JWT 相关服务接口定义
 */
public interface JwtService {

	/**
	 * 生成 JWT Token, 使用默认的超时时间
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 * @throws Exception
	 */
	String generateToken(String username, String password) throws Exception;

	/**
	 * 生成指定超时时间的 Token, 单位是天
	 *
     * @param username 用户名
     * @param password 密码
	 * @param expire 超时时间
	 * @return
	 * @throws Exception
	 */
	String generateToken(String username, String password, int expire) throws Exception;

	/**
	 * 注册用户并生成 Token 返回
	 *
	 * @param usernameAndPassword 用户名与密码
	 * @return
	 * @throws Exception
	 */
	String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception;

}
