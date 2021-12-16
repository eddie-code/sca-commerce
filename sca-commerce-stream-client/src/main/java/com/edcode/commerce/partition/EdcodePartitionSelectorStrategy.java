package com.edcode.commerce.partition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.stereotype.Component;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 决定 message 发送到哪个分区的策略
 */
@Slf4j
@Component
public class EdcodePartitionSelectorStrategy implements PartitionSelectorStrategy {

	/**
	 * 选择分区的策略
	 */
	@Override
	public int selectPartition(Object key, int partitionCount) {
		int partition = key.toString().hashCode() % partitionCount;
		log.info("SpringCloud Stream EdCode Selector info: [{}], [{}], [{}]", key.toString(), partitionCount, partition);
		return partition;
	}
}
