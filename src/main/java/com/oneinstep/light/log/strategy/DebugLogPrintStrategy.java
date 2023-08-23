package com.oneinstep.light.log.strategy;

import org.slf4j.Logger;

/**
 * debug 日志打印策略实现
 **/
public class DebugLogPrintStrategy implements com.oneinstep.light.log.strategy.LogPrintStrategy {

    @Override
    public void printLog(Logger logger, String message) {
        logger.debug("{}", message);
    }

}
