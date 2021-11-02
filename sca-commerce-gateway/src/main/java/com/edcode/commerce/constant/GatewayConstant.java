package com.edcode.commerce.constant;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 网关常量定义
 */
public class GatewayConstant {

	/** 登录 uri */
	public static final String LOGIN_URI = "/sca-commerce/login";

	/** 注册 uri */
	public static final String REGISTER_URI = "/sca-commerce/register";

	/** 去授权中心拿到登录 token 的 uri 格式化接口 */
	public static final String AUTHORITY_CENTER_TOKEN_URL_FORMAT = "http://%s:%s/scacommerce-authority-center/authority/token";

	/** 去授权中心注册并拿到 token 的 uri 格式化接口 */
	public static final String AUTHORITY_CENTER_REGISTER_URL_FORMAT = "http://%s:%s/scacommerce-authority-center/authority/register";

}
