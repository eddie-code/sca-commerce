package com.edcode.commerce.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 自定义物流消息通信信道(Source)
 */
public interface LogisticsSource {

	/** 输出信道名称 */
	String OUTPUT = "logisticsOutput";

	/**
	 * 物流 Source -> logisticsOutput
     *
     * 通信信道的名称是 logisticsOutput, 对应到 yml 文件里的配置
	 */
	@Output(LogisticsSource.OUTPUT)
	MessageChannel logisticsOutput();
}
