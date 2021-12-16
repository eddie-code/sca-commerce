package com.edcode.commerce.stream.edcode;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 自定义输出信道
 */
public interface EdcodeSource {

    String OUTPUT = "edcodeOutput";

    /** 输出信道的名称是 edcodeOutput, 需要使用 Stream 绑定器在 yml 文件中声明 */
    @Output(EdcodeSource.OUTPUT)
    MessageChannel edcodeOutput();
}
