package com.edcode.commerce.service.async;

import com.edcode.commerce.constant.AsyncTaskStatusEnum;
import com.edcode.commerce.vo.AsyncTaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 异步任务执行监控切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AsyncTaskMonitor {

    /** 注入异步任务管理器 */
    private final AsyncTaskManager asyncTaskManager;

    /**
     * 异步任务执行的环绕切面
     *
     * 环绕切面让我们可以在方法执行之前和执行之后做一些 "额外" 的操作
     *
     */
    @Around("execution(* com.edcode.commerce.service.async.AsyncServiceImpl.*(..))")
    public Object taskHandle(ProceedingJoinPoint proceedingJoinPoint) {

        // 获取 taskId, 调用异步任务传入的第二个参数
        String taskId = proceedingJoinPoint.getArgs()[1].toString();

        // 获取任务信息, 在提交任务的时候就已经放入到容器中了
        AsyncTaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);
        log.info("AsyncTaskMonitor正在监视异步任务: [{}]", taskId);

        taskInfo.setStatus(AsyncTaskStatusEnum.RUNNING);
        asyncTaskManager.setTaskInfo(taskInfo); // 设置为运行状态, 并重新放入容器

        AsyncTaskStatusEnum status;
        Object result;

        try {
            // 执行异步任务
            result = proceedingJoinPoint.proceed();
            status = AsyncTaskStatusEnum.SUCCESS;
        } catch (Throwable ex) {
            // 异步任务出现了异常
            result = null;
            status = AsyncTaskStatusEnum.FAILED;
            log.error("AsyncTaskMonitor:异步任务 [{}] 失败，错误信息: [{}]", taskId, ex.getMessage(), ex);
        }

        // 设置异步任务其他的信息, 再次重新放入到容器中
        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(status);
        taskInfo.setTotalTime(String.valueOf(
                taskInfo.getEndTime().getTime() - taskInfo.getStartTime().getTime()
        ));
        asyncTaskManager.setTaskInfo(taskInfo);

        return result;
    }
}

