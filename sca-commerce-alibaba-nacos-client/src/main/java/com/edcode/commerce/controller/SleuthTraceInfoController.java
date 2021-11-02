package com.edcode.commerce.controller;

import com.edcode.commerce.service.SleuthTraceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 打印跟踪信息
 */
@Slf4j
@RestController
@RequestMapping("/sleuth")
@RequiredArgsConstructor
public class SleuthTraceInfoController {

    private final SleuthTraceInfoService traceInfoService;

    /**
     * 打印日志跟踪信息
     */
    @GetMapping("/trace-info")
    public void logCurrentTraceInfo() {
        traceInfoService.logCurrentTraceInfo();
    }
}
