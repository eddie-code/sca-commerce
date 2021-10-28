package com.edcode.commerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户名密码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsernameAndPassword {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
