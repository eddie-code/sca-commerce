package com.edcode.commerce.filter;

import com.edcode.commerce.constant.CommonConstant;
import com.edcode.commerce.util.TokenParseUtil;
import com.edcode.commerce.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户身份统一登录拦截
 */
@SuppressWarnings("all")
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 部分请求不需要带有身份信息, 即白名单
        if (checkWhiteListUrl(request.getRequestURI())) {
            return true;
        }

        // 先尝试从 http header 里面拿到 token
        String token = request.getHeader(CommonConstant.JWT_USER_INFO_KEY);

        LoginUserInfo loginUserInfo = null;
        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("解析登录用户信息错误: [{}]", ex.getMessage(), ex);
        }

        // 如果程序走到这里, 说明 header 中没有 token 信息 （这个判断基本不会跳进来，Gateway已经拦截过了）
        if (null == loginUserInfo) {
            throw new RuntimeException("无法分析当前登录用户");
        }

        log.info("设置登录用户信息: [{}]", request.getRequestURI());

        // 设置当前请求上下文, 把用户信息填充进去
        AccessContext.setLoginUserInfo(loginUserInfo);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 【postHandle 请求执行之后，返回之前】 不需要返回之前修改
    }

    /**
     * 在请求完全结束后调用, 常用于清理资源等工作
     * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 判断 ThreadLocal 是否有用户信息
        if (null != AccessContext.getLoginUserInfo()) {
            // 清除 ThreadLocal 里面用户信息
            AccessContext.clearLoginUserInfo();
        }
    }

    /**
     * 校验是否是白名单接口
     * swagger2 接口
     * */
    private boolean checkWhiteListUrl(String url) {
        return StringUtils.containsAny(
                url,
                "springfox", "swagger", "v2",
                "webjars", "doc.html"
        );
    }
}