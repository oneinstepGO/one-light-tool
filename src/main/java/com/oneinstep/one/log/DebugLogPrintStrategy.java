package com.hst.bss.light.log.strategy;

import org.slf4j.Logger;

/**
 * debug 日志打印策略实现
 *
 * @author aaron.shaw
 * @date 2023-05-11 22:38
 **/
public class DebugLogPrintStrategy implements LogPrintStrategy {

    @Override
    public void printLog(Logger logger, String message) {
        logger.debug("{}", message);
    }

}