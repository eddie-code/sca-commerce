package com.edcode.commerce.block_handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.edcode.commerce.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author eddie.lee
 * @description 自定义通用的限流处理逻辑
 */
@Slf4j
public class BlockHandler {

    /**
     * 需要静态方法，才能被调用
     */
    public static CommonResponse<String> handleBlockException(BlockException exception) {
        log.error("触发块处理程序: [{}], [{}]", JSON.toJSONString(exception.getRule()), exception.getRuleLimitApp());
        return new CommonResponse<>(
                -1,
                "流规则触发块异常",
                null
        );
    }

}
