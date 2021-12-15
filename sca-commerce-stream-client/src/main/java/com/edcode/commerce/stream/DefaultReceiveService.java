package com.edcode.commerce.stream;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用默认的信道实现消息的接收
 */
@Slf4j
@EnableBinding(Sink.class)
public class DefaultReceiveService {

	/**
	 * 使用默认的输入信道接收消息
	 * 
	 * @param payload
	 */
	@StreamListener(Sink.INPUT)
	public void receiveMessage(Object payload) {
		log.info("在 DefaultReceiveService 消费消息中启动  ");
		MessageVo edcodeMessage = JSON.parseObject(payload.toString(), MessageVo.class);
		// 消费消息
		log.info("在 DefaultReceiveService 中使用消息成功: [{}]", JSON.toJSONString(edcodeMessage));
	}

}
