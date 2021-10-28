package com.edcode.commerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 登录用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserInfo {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

}
