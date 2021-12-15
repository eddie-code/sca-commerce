package com.edcode.commerce.stream;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.vo.MessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用默认的通信信道实现消息的发送
 */
@Slf4j
@EnableBinding(Source.class)
@RequiredArgsConstructor
public class DefaultSendService {

    private final Source source;

    /**
     * <h2>使用默认的输出信道发送消息</h2>
     * */
    public void sendMessage(MessageVo message) {

        String jsonString = JSON.toJSONString(message);
        log.info("在 DefaultSendService 中发送消息: [{}]", jsonString);

        // Spring Messaging, 统一消息的编程模型, 是 Stream 组件的重要组成部分之一
        source.output().send(MessageBuilder.withPayload(jsonString).build());
    }

}
