package com.hst.bss.light.async.context;

import com.hst.arch.components.response.BaseResponseDTO;
import com.hst.bss.light.async.config.TaskConfig;
import com.hst.bss.light.log.enums.PrintLogLevel;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步任务执行上下文
 *
 * @author aaron.shaw
 */
@Data
public class AsyncTaskContext {
    /**
     * 请求上下文参数
     */
    private Map<String, Map<String, Object>> allTaskParamMap;
    /**
     * 任务配置
     */
    @NotNull
    @Valid
    private TaskConfig taskConfig;
    /**
     * 保存任务结果
     */
    private Map<String, BaseResponseDTO<?>> taskResultMap = new ConcurrentHashMap<>();

    /**
     * 运行时上下文 存储一些临时变量
     * 注意 key 不要重复
     */
    private Map<String, Object> runtimeContextMap = new ConcurrentHashMap<>();

    /**
     * 异步框架打印的日志级别
     */
    private PrintLogLevel logLevel = PrintLogLevel.DEBUG;
}
