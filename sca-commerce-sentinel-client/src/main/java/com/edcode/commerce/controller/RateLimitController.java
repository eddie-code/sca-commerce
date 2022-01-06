package com.edcode.commerce.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.edcode.commerce.block_handler.BlockHandler;
import com.edcode.commerce.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @description 基于 Sentinel 控制台配置流控规则
 * <p>
 * Sentinel 是懒加载的, 先去访问一下, 就可以在 Sentinel Dashboard 看到了
 */
@Slf4j
@RestController
@RequestMapping("/dashboard")
public class RateLimitController {

    /**
     * 在 dashboard 中 "流控规则" 中按照资源名称新增流控规则
     *
     * @return
     */
    @GetMapping("/by-resource")
    @SentinelResource(
            value = "byResource",
            blockHandler = "handleBlockException",
            blockHandlerClass = BlockHandler.class
    )
    public CommonResponse<String> byResource() {
        log.info("按资源进入速率限制控制器");
        return new CommonResponse<>(0, "", "byResource");
    }

    /**
     * 在 "簇点链路" 中给 url 添加流控规则
     *
     * @return
     */
    @GetMapping("/by-url")
    @SentinelResource(value = "byUrl")
    public CommonResponse<String> byUrl() {
        log.info("通过 url 进入速率限制控制器");
        return new CommonResponse<>(0, "", "byUrl");
    }

}
