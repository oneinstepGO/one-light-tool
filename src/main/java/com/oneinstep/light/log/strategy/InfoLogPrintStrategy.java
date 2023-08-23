package com.oneinstep.light.log.strategy;

import org.slf4j.Logger;

/**
 * info 日志打印策略实现
 *
 *
 **/
public class InfoLogPrintStrategy implements LogPrintStrategy {

    @Override
    public void printLog(Logger logger, String message) {
        logger.info("{}", message);
    }

}
