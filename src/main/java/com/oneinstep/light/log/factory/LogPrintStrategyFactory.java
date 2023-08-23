package com.oneinstep.light.log.factory;

import com.oneinstep.light.log.enums.PrintLogLevel;
import com.oneinstep.light.log.strategy.*;
import lombok.experimental.UtilityClass;

/**
 * 创建日志打印策略工厂
 *
 * 
 **/
@UtilityClass
public class LogPrintStrategyFactory {

    /**
     * 创建 日志打印策略
     *
     * @param logLevel 日志级别
     * @return 日志打印策略
     */
    public static LogPrintStrategy createLogPrintStrategy(PrintLogLevel logLevel) {
        switch (logLevel) {
            case INFO:
                return new InfoLogPrintStrategy();
            case DEBUG:
                return new DebugLogPrintStrategy();
            case WARN:
                return new WarnLogPrintStrategy();
            case ERROR:
                return new ErrorLogPrintStrategy();
            default:
                throw new IllegalArgumentException("Unsupported log level: " + logLevel);
        }
    }

}
