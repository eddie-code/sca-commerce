package com.edcode.commerce.stream.edcode;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.vo.MessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用自定义的通信信道 QinyiSource 实现消息的发送
 */
@Slf4j
@EnableBinding(EdcodeSource.class)
@RequiredArgsConstructor
public class EdcodeSendService {

	private final EdcodeSource edcodeSource;

	public void sendMessage(MessageVo message) {

		String jsonString = JSON.toJSONString(message);
		log.info("在 EdcodeSendService 中发送消息: [{}]", jsonString);
		edcodeSource.edcodeOutput().send(MessageBuilder.withPayload(jsonString).build());
	}

}
