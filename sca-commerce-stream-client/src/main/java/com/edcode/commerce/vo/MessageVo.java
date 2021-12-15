package com.edcode.commerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 消息传递对象: SpringCloud Stream + Kafka/RocketMQ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo {

	private Integer id;

	private String projectName;

	private String org;

	private String author;

	private String version;

    /**
     * 返回一个默认的消息, 方便使用
     */
    public static MessageVo defaultMessage() {

        return new MessageVo(
                1,
                "sca-commerce-stream-client",
                "blog.eddilee.cn",
                "Eddie",
                "1.0"
        );
    }

}
