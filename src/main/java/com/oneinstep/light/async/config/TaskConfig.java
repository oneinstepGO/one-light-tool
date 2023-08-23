package com.oneinstep.light.async.config;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * 任务配置
 */
@Data
public class TaskConfig {
    /**
     * 任务编排规则
     * 第一个 List<String> 最开始执行，最简单的可以只有一个 List<String>
     * 中间的数个  List<String> 可并行执行
     * 最后一个 List<String> 最后执行
     * List<String> 中每个元素（字符串）的编排规则：
     * 冒号 ":" 表示由依赖的父子任务，例如 A:B 表示任务A执行完成后执行任务B
     * 逗号 "," 表示之间无依赖的任务，可并行执行，例如 A,B,C 表示任务A,任务B,任务C 并行执行
     * A,B,C:D 表示任务A,任务B,任务C 都执行完成后，然后再执行任务D
     */
    @NotNull
    @Size(min = 1)
    private List<List<String>> arrangeRules;
    /**
     * 任务配置详情
     */
    @NotNull
    private Map<String, com.oneinstep.light.async.config.TaskDetail> taskDetailsMap;
}
