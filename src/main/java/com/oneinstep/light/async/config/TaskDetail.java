package com.oneinstep.light.async.config;

import lombok.Data;

/**
 * 任务详情
 */
@Data
public class TaskDetail {
    /**
     * 任务标识
     */
    private String taskId;
    /**
     * 任务执行类的全限定类名
     */
    private String fullClassName;
}
