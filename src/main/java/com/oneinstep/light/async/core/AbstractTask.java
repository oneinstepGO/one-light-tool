package com.oneinstep.light.async.core;

import com.oneinstep.light.api.Response;
import com.oneinstep.light.async.context.AsyncTaskContext;
import com.oneinstep.light.log.enums.PrintLogLevel;
import com.oneinstep.light.log.strategy.LogPrintStrategy;
import lombok.Getter;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.oneinstep.light.log.factory.LogPrintStrategyFactory.createLogPrintStrategy;

/**
 * 抽象任务
 * 自己的任务逻辑需要 继承 AbstractTask
 * 一些自定义操作 可覆写相关方法
 */
@Getter
public abstract class AbstractTask<T> implements Consumer<AsyncTaskContext> {

    /**
     * 任务ID
     * -- GETTER --
     *  获取任务ID
     *
     * @return 任务ID

     */
    protected String taskId;

    /**
     * 任务的执行逻辑
     *
     * @param asyncTaskContext 请求上下文
     * @param taskParamMap     该任务的参数map
     * @return 单个任务执行结果
     */
    protected abstract Response<T> invoke(AsyncTaskContext asyncTaskContext, Map<String, Object> taskParamMap);

    /**
     * 任务调用前
     *
     * @param asyncTaskContext 请求上下文
     * @param taskParamMap     该任务的参数map
     */
    protected void beforeInvoke(AsyncTaskContext asyncTaskContext, Map<String, Object> taskParamMap) {
    }

    /**
     * 任务调用后
     *
     * @param asyncTaskContext 请求上下文
     * @param result           任务中执行结果
     * @param taskParamMap     该任务的参数map
     */
    protected void afterInvoke(AsyncTaskContext asyncTaskContext, Response<T> result, Map<String, Object> taskParamMap) {
    }

    /**
     * 获取日志
     *
     * @return 日志打印类
     */
    protected abstract Logger getLogger();

    /**
     * 该任务打印日志级别 默认为 INFO
     * 需要自定以该任务级别，覆写该方法即可
     * <pre>@Override
     *     protected PrintLogLevel getTaskLogLevel() {
     *         return INFO;
     *     }
     * </pre>
     *
     * @return 该任务打印日志级别
     */
    protected PrintLogLevel getTaskLogLevel() {
        return PrintLogLevel.DEBUG;
    }

    /**
     * 检查任务结果是否符合预期
     * 可覆写该方法，实现自己的任务结果校验逻辑
     *
     * @param asyncTaskContext 请求上下文
     * @param result           任务执行结果
     * @param taskParamMap     该任务的参数map
     * @return 是否通过检查
     */
    protected boolean checkResult(AsyncTaskContext asyncTaskContext, Response<T> result, Map<String, Object> taskParamMap) {
        return true;
    }

    /**
     * 模板方法
     * 执行任务
     *
     * @param asyncTaskContext 请求上下文
     */
    @Override
    public void accept(AsyncTaskContext asyncTaskContext) {

        PrintLogLevel taskLogLevel = getTaskLogLevel();
        LogPrintStrategy logPrintStrategy = createLogPrintStrategy(taskLogLevel);

        logPrintStrategy.printLog(getLogger(), String.format("beforeInvoke ==> taskId:%s.", getTaskId()));

        Map<String, Object> taskParamMap = null;
        Map<String, Map<String, Object>> allTaskParamMap = asyncTaskContext.getAllTaskParamMap();
        if (MapUtils.isNotEmpty(allTaskParamMap)) {
            taskParamMap = allTaskParamMap.get(getTaskId());
        }
        taskParamMap = taskParamMap == null ? new HashMap<>() : taskParamMap;
        beforeInvoke(asyncTaskContext, taskParamMap);
        logPrintStrategy.printLog(getLogger(), String.format("invoke ==> taskId:%s.", getTaskId()));
        Response<T> result = invoke(asyncTaskContext, taskParamMap);
        logPrintStrategy.printLog(getLogger(), String.format("The Result of taskId:%s -> %s", getTaskId(), result == null ? "null" : result.toString()));
        if (checkResult(asyncTaskContext, result, taskParamMap)) {
            logPrintStrategy.printLog(getLogger(), String.format("Result of taskId:%s check success.", getTaskId()));
            asyncTaskContext.getTaskResultMap().putIfAbsent(getTaskId(), result);
            logPrintStrategy.printLog(getLogger(), String.format("afterInvoke ==> taskId:%s.", getTaskId()));
            afterInvoke(asyncTaskContext, result, taskParamMap);
        } else {
            logPrintStrategy.printLog(getLogger(), String.format("Result of taskId:%s check invalid.", getTaskId()));
        }
    }

    /**
     * 设置任务ID
     *
     * @param taskId 任务ID
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
