package com.edcode.commerce.constant;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 通用模块常量定义
 */
public final class CommonConstant {

	/**
	 * RSA 公钥
	 */
	public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkKYIzyO0COH0LSbitj2RObIh6wAt8yrg8IsHPIQ7C1ogmuI7"
			+ "WIf53uuzVF5Vjc73Jp8K7zoLZgLK/vXWQZrmrKYoH7p4jZOID1sE4Pzh0jk1vZtTvfvLtyNLvdxvwUk8YdrwurUxT7LDc9Jhc1WOfG6hVNjpS9mnH+v2ablVIj39"
			+ "azFqUgQ8qOBzvg5TZsxiwb4b7KnDRWhY+t8f+T+KKIINVzCp7eWw0wIu+51GBb8KS/zX+gfe5FKo0o68l42YSsMYEJsI61Xzl5+WweW6YvQwgF30WWPCTUlQygIA"
			+ "2H4eQVtuLiN5V2pXHdnKuDyIvJDbOzkym0bAUMjuNamK1wIDAQAB";

	/**
	 * JWT 中存储用户信息的 key
	 */
	public static final String JWT_USER_INFO_KEY = "sca-commerce-user";

    /**
     * 授权中心的 service-id
     *
     * spring:
     *   application:
     *     name: sca-commerce-authority-center
     */
    public static final String AUTHORITY_CENTER_SERVICE_ID = "sca-commerce-authority-center";

}
