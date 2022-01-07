package com.edcode.commerce.fallback_handler;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.vo.JwtToken;
import com.edcode.commerce.vo.UsernameAndPassword;
import lombok.extern.slf4j.Slf4j;

/**
 * @author eddie.lee
 * @description Sentinel 回退降级的兜底策略
 */
@Slf4j
public class FallbackHandler {

    /**
     * getTokenFromAuthorityService 方法的 fallback
     *
     * @param usernameAndPassword
     * @return
     */
    public static JwtToken getTokenFromAuthorityServiceFallback(
            UsernameAndPassword usernameAndPassword
    ) {
        log.error("从授权服务回退获取令牌: [{}]",
                JSON.toJSONString(usernameAndPassword));
        return new JwtToken("sca-edcode-fallback");
    }

    /**
     * ignoreException 方法的 fallback
     *
     * @param code
     * @return
     */
    public static JwtToken ignoreExceptionFallback(Integer code) {
        log.error("忽略异常输入代码: [{}] 有触发异常", code);
        return new JwtToken("edcode-sca-fallback");
    }

}
