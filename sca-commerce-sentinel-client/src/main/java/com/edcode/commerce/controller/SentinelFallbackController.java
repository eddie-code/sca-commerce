package com.edcode.commerce.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.edcode.commerce.vo.JwtToken;
import com.edcode.commerce.vo.UsernameAndPassword;
import com.netflix.ribbon.hystrix.FallbackHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author eddie.lee
 * @description Sentinel 提供容错降级的功能
 */
@SuppressWarnings("all")
@Slf4j
@RestController
@RequestMapping("/sentinel-fallback")
@RequiredArgsConstructor
public class SentinelFallbackController {

    /**
     * 注入没有增强的 RestTemplate
     */
    private final RestTemplate restTemplate;

    /**
     *
     * @param usernameAndPassword
     * @return
     */
    @PostMapping("/get-token")
    @SentinelResource(
            value = "getTokenFromAuthorityService",
            fallback = "getTokenFromAuthorityServiceFallback",
            fallbackClass = { FallbackHandler.class }
    )
    public JwtToken getTokenFromAuthorityService(
            @RequestBody UsernameAndPassword usernameAndPassword) {

        String requestUrl =
                "http://127.0.0.1:7000/scacommerce-authority-center/authority/token";
        log.info("RestTemplate request url and body: [{}], [{}]",
                requestUrl, JSON.toJSONString(usernameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers),
                JwtToken.class
        );
    }

    /**
     * 让 Sentinel 忽略一些异常
     * @param code
     * @return
     */
    @GetMapping("/ignore-exception")
    @SentinelResource(
            value = "ignoreException",
            fallback = "ignoreExceptionFallback",
            fallbackClass = { FallbackHandler.class },
            exceptionsToIgnore = { NullPointerException.class }
    )
    public JwtToken ignoreException(@RequestParam Integer code) {

        if (code % 2 == 0) {
            throw new NullPointerException("you input code is: " + code);
        }

        return new JwtToken("edcode-sca");
    }

}
