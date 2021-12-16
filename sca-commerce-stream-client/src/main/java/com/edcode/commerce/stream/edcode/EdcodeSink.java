package com.edcode.commerce.stream.edcode;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 自定义输入信道
 */
public interface EdcodeSink {

    String INPUT = "edcodeInput";

    /** 输入信道的名称是 edcodeInput, 需要使用 Stream 绑定器在 yml 文件中配置*/
    @Input(EdcodeSink.INPUT)
    SubscribableChannel edcodeInput();

}
