package com.edcode.commerce.service.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edcode.commerce.constant.AsyncTaskStatusEnum;
import com.edcode.commerce.goods.GoodsInfo;
import com.edcode.commerce.vo.AsyncTaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    private final StringRedisTemplate redisTemplate;

    private final IAsyncService asyncService;

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
        redisTemplate.opsForValue().set(taskInfo.getTaskId(), JSON.toJSONString(taskInfo),1000 , TimeUnit.SECONDS);
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
        redisTemplate.opsForValue().set(taskInfo.getTaskId(), JSON.toJSONString(taskInfo),1000 , TimeUnit.SECONDS);
    }

    /**
     * 获取异步任务执行状态信息
     */
	public AsyncTaskInfo getTaskInfo(String taskId) {
		JSONObject asyncTaskInfo = JSONObject.parseObject(
		        redisTemplate.opsForValue().get(taskId)
        );
		return JSON.toJavaObject(asyncTaskInfo, AsyncTaskInfo.class);
	}

}
