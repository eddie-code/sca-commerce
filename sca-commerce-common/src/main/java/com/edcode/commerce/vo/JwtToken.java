package com.edcode.commerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 授权中心鉴权之后给客户端的 Token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    /**
     * JWT
     */
    private String token;

}
