package com.edcode.commerce.service.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edcode.commerce.constant.AsyncTaskConstant;
import com.edcode.commerce.constant.AsyncTaskStatusEnum;
import com.edcode.commerce.goods.GoodsInfo;
import com.edcode.commerce.vo.AsyncTaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 异步任务执行管理器
 *
 *              对异步任务进行包装管理, 记录并塞入异步任务执行信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncTaskManager {

    private final Map<String, AsyncTaskInfo> taskContainer = new HashMap<>(16);

    private final IAsyncService asyncService;

    public final StringRedisTemplate stringRedisTemplate;

    /**
     * 初始化异步任务
     */
    public AsyncTaskInfo initTask() {

        AsyncTaskInfo taskInfo = new AsyncTaskInfo();
        // 设置一个唯一的异步任务 id, 只要唯一即可
        taskInfo.setTaskId(UUID.randomUUID().toString());
        taskInfo.setStatus(AsyncTaskStatusEnum.STARTED);
        taskInfo.setStartTime(new Date());

        // 初始化的时候就要把异步任务执行信息放入到存储容器中
//        taskContainer.put(taskInfo.getTaskId(), taskInfo);

        stringRedisTemplate.opsForValue().set(AsyncTaskConstant.SCACOMMERCE_ASYNC_TASK_KEY + taskInfo.getTaskId(), JSON.toJSONString(taskInfo), 3600, TimeUnit.SECONDS);

        return taskInfo;
    }

    /**
     * 提交异步任务
     */
    public AsyncTaskInfo submit(List<GoodsInfo> goodsInfos) {

        // 初始化一个异步任务的监控信息
        AsyncTaskInfo taskInfo = initTask();
        asyncService.asyncImportGoods(goodsInfos, taskInfo.getTaskId());
        return taskInfo;
    }

    /**
     * 设置异步任务执行状态信息
     */
    public void setTaskInfo(AsyncTaskInfo taskInfo) {
//        taskContainer.put(taskInfo.getTaskId(), taskInfo);

        stringRedisTemplate.opsForValue().set(AsyncTaskConstant.SCACOMMERCE_ASYNC_TASK_KEY + taskInfo.getTaskId(), JSON.toJSONString(taskInfo), 3600, TimeUnit.SECONDS);
    }
    /**
     * 获取异步任务执行状态信息
     */
    public AsyncTaskInfo getTaskInfo(String taskId) {
        return JSON.parseObject(
                stringRedisTemplate.opsForValue().get(AsyncTaskConstant.SCACOMMERCE_ASYNC_TASK_KEY + taskId),
                AsyncTaskInfo.class
        );
        //        return taskContainer.get(taskId);
    }
}
