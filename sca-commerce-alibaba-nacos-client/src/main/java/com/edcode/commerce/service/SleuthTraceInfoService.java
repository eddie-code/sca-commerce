package com.edcode.commerce.service;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 使用代码更直观的看到 Sleuth 生成的相关跟踪信息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SleuthTraceInfoService {

    /** brave.Tracer 跟踪对象 */
    private final Tracer tracer;

    /**
     * 打印当前的跟踪信息到日志中
     */
    public void logCurrentTraceInfo() {

        log.info("Sleuth trace id: [{}]", tracer.currentSpan().context().traceId());
        log.info("Sleuth span id: [{}]", tracer.currentSpan().context().spanId());
    }
}
