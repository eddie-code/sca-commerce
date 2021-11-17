package com.edcode.commerce.controller;

import com.edcode.commerce.goods.GoodsInfo;
import com.edcode.commerce.service.async.AsyncTaskManager;
import com.edcode.commerce.vo.AsyncTaskInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 异步任务服务对外提供的 API
 */
@Api(tags = "商品异步入库服务")
@Slf4j
@RestController
@RequestMapping("/async-goods")
@RequiredArgsConstructor
public class AsyncGoodsController {

    /**
     * 异步任务执行管理器
     */
    private final AsyncTaskManager asyncTaskManager;

    @ApiOperation(value = "导入商品", notes = "导入商品进入到商品表", httpMethod = "POST")
    @PostMapping("/import-goods")
    public AsyncTaskInfo importGoods(@RequestBody List<GoodsInfo> goodsInfos) {
        // 提交异步任务
        return asyncTaskManager.submit(goodsInfos);
    }

    @ApiOperation(value = "查询状态", notes = "查询异步任务的执行状态", httpMethod = "GET")
    @GetMapping("/task-info")
    public AsyncTaskInfo getTaskInfo(@RequestParam String taskId) {
        // 获取异步任务执行状态信息
        return asyncTaskManager.getTaskInfo(taskId);
    }

}
