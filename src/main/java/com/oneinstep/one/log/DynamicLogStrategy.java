package com.hst.bss.light.log.strategy;

/**
 * 动态日志策略，可基于 nacos 做动态日志级别调整
 * 如需改变策略，可覆写getLoggerLevel方法
 * <pre>
 *    <pre> @Component
 * public class MyDynamicLog implements DynamicLogStrategy {
 *     <pre>@Override
 *     public String getLoggerLevel() {
 *         // 也可以配置在nacos, 自动刷新
 *         return "debug";
 *     }
 * }
 * </pre>
 *
 * @author aaron.shaw
 * @date 2023-05-14 11:27
 **/
public interface DynamicLogStrategy {

    /**
     * 获取日志级别 info | debug | error | warn
     * 大小写均可
     *
     * @return 日志级别
     */
    String getLoggerLevel();

}
