package com.edcode.commerce.advice;

import com.edcode.commerce.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 全局异常捕获处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

	@ExceptionHandler(value = Exception.class)
	public CommonResponse<String> handlerCommerceException(HttpServletRequest request, Exception ex) {

		CommonResponse<String> response = new CommonResponse<>(-1, "GlobalExceptionAdvice ==> business error");

		response.setData(ex.getMessage());

		log.error("GlobalExceptionAdvice ==> commerce service has error: [{}]", ex.getMessage(), ex);

		return response;
	}

}
