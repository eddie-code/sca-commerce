package com.edcode.commerce.filter;

import com.edcode.commerce.vo.LoginUserInfo;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description
 *
 * 使用 ThreadLocal 去单独存储每一个线程携带的 LoginUserInfo 信息
 * 要及时的清理我们保存到 ThreadLocal 中的用户信息:
 * 1. 保证没有资源泄露
 * 2. 保证线程在重用时, 不会出现数据混乱
 *
 */
public class AccessContext {

    /**
     * 用户信息保存在 ThreadLocal 里面
     */
    private static final ThreadLocal<LoginUserInfo> LOGIN_USER_INFO = new ThreadLocal<>();

    /**
     * 从 ThreadLocal 获取用户信息
     */
    public static LoginUserInfo getLoginUserInfo() {
        return LOGIN_USER_INFO.get();
    }

    /**
     * 从拦截器或者 jwt 信息， set 到 ThreadLocal
     */
    public static void setLoginUserInfo(LoginUserInfo loginUserInfo) {
        LOGIN_USER_INFO.set(loginUserInfo);
    }

    /**
     * 清除 ThreadLocal 里面的当前线程用户信息 （必需要有，不然会 OOM ）
     */
    public static void clearLoginUserInfo() {
        LOGIN_USER_INFO.remove();
    }
}
