package com.edcode.commerce.controller;

import com.edcode.commerce.stream.DefaultSendService;
import com.edcode.commerce.stream.edcode.EdcodeSendService;
import com.edcode.commerce.vo.MessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 构建消息驱动
 */
@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

	private final DefaultSendService defaultSendService;

	private final EdcodeSendService edcodeSendService;

	/**
	 * 默认信道
	 */
	@GetMapping("/default")
	public void defaultSend() {
		defaultSendService.sendMessage(MessageVo.defaultMessage());
	}

	/**
	 * 自定义信道
	 */
	@GetMapping("/edcode")
	public void qinyiSend() {
		edcodeSendService.sendMessage(MessageVo.defaultMessage());
	}

}
