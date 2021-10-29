package com.edcode.commerce.util;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.constant.CommonConstant;
import com.edcode.commerce.vo.LoginUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description JWT Token 解析工具类
 */
@Slf4j
public class TokenParseUtil {

    /**
     * 从 JWT Token中解析 LoginUserInfo 对象
     * 
     * @param token
     * @return
     */
	public static LoginUserInfo parseUserInfoFromToken(String token) {
        
        if (null == token) {
            return null;
        }

		try {
            log.info("公钥：{}",getPublicKey());
			Jws<Claims> claimsJws = parseToken(token, getPublicKey());
			Claims body = claimsJws.getBody();

			// 如果 Token 已经过期了, 返回 null
			if (body.getExpiration().before(Calendar.getInstance().getTime())) {
				return null;
			}

			// 返回 Token 中保存的用户信息
            return JSON.parseObject(
                    body.get(CommonConstant.JWT_USER_INFO_KEY).toString(),
                    LoginUserInfo.class
            );
			
		} catch (Exception e) {
			log.error("通过公钥去解析 JWT Token 错误：[{}]", e.getMessage());
			return null;
		}
        
    }

    /**
     * 通过公钥去解析 JWT Token
     * 
     * @param token
     * @param publicKey
     * @return
     */
    private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 根据本地存储的公钥获取到 PublicKey 对象
     * 
     * @return
     */
    private static PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(CommonConstant.PUBLIC_KEY)
        );
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

}
