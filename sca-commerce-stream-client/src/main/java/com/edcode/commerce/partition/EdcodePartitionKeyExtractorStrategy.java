package com.edcode.commerce.partition;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 自定义从 Message 中提取 partition key 的策略
 */
@Slf4j
@Component
public class EdcodePartitionKeyExtractorStrategy implements PartitionKeyExtractorStrategy {

	@Override
	public Object extractKey(Message<?> message) {
		MessageVo messageVo = JSON.parseObject(message.getPayload().toString(), MessageVo.class);
		// 自定义提取 key
		String key = messageVo.getProjectName();
		log.info("SpringCloud Stream EdCode Partition Key: [{}]", key);
		return key;
	}
}
