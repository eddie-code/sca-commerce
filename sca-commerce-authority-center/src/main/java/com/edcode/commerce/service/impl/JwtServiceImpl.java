package com.edcode.commerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.conf.AppProperties;
import com.edcode.commerce.constant.CommonConstant;
import com.edcode.commerce.dao.ScacommerceUserDao;
import com.edcode.commerce.entity.ScaCommerceUser;
import com.edcode.commerce.service.JwtService;
import com.edcode.commerce.vo.LoginUserInfo;
import com.edcode.commerce.vo.UsernameAndPassword;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description JWT 相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    
    private final ScacommerceUserDao scacommerceUserDao;

    private final AppProperties appProperties;

    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username, password, 0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {

        // 首先需要验证用户是否能够通过授权校验, 即输入的用户名和密码能否匹配数据表记录
        ScaCommerceUser scaCommerceUser = scacommerceUserDao.findByUsernameAndPassword(username, password);

        if (null == scaCommerceUser) {
            log.error("can not find user: [{}], [{}]", username, password);
            return null;
        }

        // Token 中塞入对象, 即 JWT 中存储的信息, 后端拿到这些信息就可以知道是哪个用户在操作
        LoginUserInfo loginUserInfo = new LoginUserInfo(scaCommerceUser.getId(), scaCommerceUser.getUsername());

        // 小于 0 就重新赋值
        if (expire <= 0) {
            expire = appProperties.getRsa().getDefaultExpireDay();
        }

        // 计算超时时间
        ZonedDateTime zdt = LocalDate.now()
                                .plus(expire, ChronoUnit.DAYS)
                                .atStartOfDay(ZoneId.systemDefault());

        Date expireDate = Date.from(zdt.toInstant());

        return Jwts.builder()
                // jwt payload --> K,V
                .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();

    }

    @Override
    public String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception {
        // 先去校验用户名是否存在, 如果存在, 不能重复注册
        ScaCommerceUser oldUser = scacommerceUserDao.findByUsername(
                usernameAndPassword.getUsername());
        if (null != oldUser) {
            log.error("username is registered: [{}]", oldUser.getUsername());
            return null;
        }

        ScaCommerceUser scaCommerceUser = new ScaCommerceUser();
        scaCommerceUser.setUsername(usernameAndPassword.getUsername());
        // MD5 编码以后的密码
        scaCommerceUser.setPassword(usernameAndPassword.getPassword());
        scaCommerceUser.setExtraInfo("{}");

        // 注册一个新用户, 写一条记录到数据表中
        scaCommerceUser = scacommerceUserDao.save(scaCommerceUser);
        log.info("register user success: [{}], [{}]", scaCommerceUser.getUsername(), scaCommerceUser.getId());

        // 生成 token 并返回
        return generateToken(scaCommerceUser.getUsername(), scaCommerceUser.getPassword());
    }

    /**
     * 根据本地存储的私钥获取到 PrivateKey 对象
     **/
    private PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec pricks8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(appProperties.getRsa().getPrivateKey()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(pricks8);
    }

}
