package com.hst.bss.light.log.strategy;

import org.slf4j.Logger;

/**
 * info 日志打印策略实现
 *
 * @author aaron.shaw
 * @date 2023-05-11 22:37
 **/
public class ErrorLogPrintStrategy implements LogPrintStrategy {

    @Override
    public void printLog(Logger logger, String message) {
        logger.error("{}", message);
    }

}
