package com.hst.bss.light.async.config;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * 任务配置
 * <pre>
 *     配置示例1 任务A,任务B,任务C, 并行执行完成后执行 任务D
 *     {
 *   "arrangeRules": [
 *     [
 *       "A,B,C:D"
 *     ]
 *   ],
 *   "taskDetailsMap": {
 *     "A": {
 *       "taskId": "A",
 *       "fullClassName": "com.hst.bss.light.async.task1.TaskA"
 *     },
 *     "B": {
 *       "taskId": "B",
 *       "fullClassName": "com.hst.bss.light.async.task1.TaskB"
 *     },
 *     "C": {
 *       "taskId": "C",
 *       "fullClassName": "com.hst.bss.light.async.task1.TaskC"
 *     },
 *     "D": {
 *       "taskId": "D",
 *       "fullClassName": "com.hst.bss.light.async.task1.TaskD"
 *     }
 *   }
 * }
 * </pre>
 * <pre>
 *     复杂配置示例2：
 *     {
 *   "arrangeRules": [
 *     [
 *       "1,2"
 *     ],
 *     [
 *       "1001,1002",
 *       "1001:1003",
 *       "1002:1005",
 *       "1001,1002:1004",
 *       "1003,1004,1005:1006"
 *     ],
 *     [
 *       "9999"
 *     ]
 *   ],
 *   "taskDetailsMap": {
 *     "1": {
 *       "taskId": "1",
 *       "fullClassName": "com.hst.bss.light.async.task.Task1"
 *     },
 *     "2": {
 *       "taskId": "2",
 *       "fullClassName": "com.hst.bss.light.async.task.Task2"
 *     },
 *     "1001": {
 *       "taskId": "1001",
 *       "fullClassName": "com.hst.bss.light.async.task.Task1001"
 *     },
 *     "1002": {
 *       "taskId": "1002",
 *       "fullClassName": "com.hst.bss.light.async.task.Task1002"
 *     },
 *     "1003": {
 *       "taskId": "1003",
 *       "fullClassName": "com.hst.bss.light.async.task.Task1003"
 *     },
 *     "1004": {
 *       "taskId": "1004",
 *       "fullClassName": "com.hst.bss.light.async.task.Task1004"
 *     },
 *     "1005": {
 *       "taskId": "1005",
 *       "fullClassName": "com.hst.bss.light.async.task.Task1005"
 *     },
 *     "1006": {
 *       "taskId": "1006",
 *       "fullClassName": "com.hst.bss.light.async.task.Task1006"
 *     },
 *     "9999": {
 *       "taskId": "9999",
 *       "fullClassName": "com.hst.bss.light.async.task.Task9999"
 *     }
 *   }
 * }
 * </pre>
 *
 * @author aaron.shaw
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
    private Map<String, TaskDetail> taskDetailsMap;
}
