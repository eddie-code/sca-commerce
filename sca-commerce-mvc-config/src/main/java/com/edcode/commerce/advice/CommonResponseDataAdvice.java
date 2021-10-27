package com.edcode.commerce.advice;

import com.edcode.commerce.annotation.IgnoreResponseAdvice;
import com.edcode.commerce.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 实现统一响应 <br>
 *              使用 @ControllerAdvice（@RestControllerAdvice） & ResponseBodyAdvice 拦截Controller方法默认返回参数，统一处理返回值/响应体
 */
@Slf4j
@RestControllerAdvice(value = "com.edcode.commerce")
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

	/**
	 * <h2>判断是否需要对响应进行处理</h2>
	 */
	@Override
	@SuppressWarnings("all")
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {

	    // 如果类给标识 IgnoreResponseAdvice 注解，就不做处理返回false
		if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
			return false;
		}

        // 如果当前方法给标识 IgnoreResponseAdvice 注解，就不做处理返回false
		if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)) {
			return false;
		}

		return true;
	}

	@Override
	@SuppressWarnings("all")
	public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

		log.debug("CommonResponseDataAdvice ==> beforeBodyWrite: [{}], [{}]", o.toString(), methodParameter);

		// 定义最终的返回对象
		CommonResponse<Object> response = new CommonResponse<>(0, "");

		if (null == o) {
		    // 如果响应为空，就不需要包装
			return response;
		} else if (o instanceof CommonResponse) {
		    // 如果是 CommonResponse 也不需要包装
			response = (CommonResponse<Object>) o;
		} else {
		    // 如果不是 null 也不是 CommonResponse 类型， 就赋值成 CommonResponse 类型
			response.setData(o);
		}

		return response;
	}
}
