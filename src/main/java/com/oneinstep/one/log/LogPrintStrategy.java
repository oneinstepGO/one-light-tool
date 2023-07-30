package com.hst.bss.light.log.strategy;

import org.slf4j.Logger;

/**
 * 日志打印策略
 *
 * @author aaron.shaw
 */
public interface LogPrintStrategy {
    /**
     * 打印进入方法日志
     *
     * @param logger  日志类
     * @param message 日志消息
     */
    void printLog(Logger logger, String message);

}




