package com.edcode.commerce.stream.edcode;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用自定义的输入信道实现消息的接收
 */
@Slf4j
@EnableBinding(EdcodeSink.class)
public class EdcodeReceiveService {

	/** 使用自定义的输入信道接收消息 */
	@StreamListener(EdcodeSink.INPUT)
	public void receiveMessage(@Payload Object payload) {
		log.info("在 EdcodeReceiveService 消费消息启动");

		MessageVo edcodeMessage = JSON.parseObject(payload.toString(), MessageVo.class);

		log.info("在 EdcodeReceiveService 中使用消息成功: [{}]", JSON.toJSONString(edcodeMessage));
	}

}
