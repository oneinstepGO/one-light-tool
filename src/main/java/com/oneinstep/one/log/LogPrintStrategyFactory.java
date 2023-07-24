package com.hst.bss.light.log.factory;

import com.hst.bss.light.log.enums.PrintLogLevel;
import com.hst.bss.light.log.strategy.*;
import lombok.experimental.UtilityClass;

/**
 * 创建日志打印策略工厂
 *
 * @author aaron.shaw
 * @date 2023-05-11 22:41
 **/
@UtilityClass
public class LogPrintStrategyFactory {

    /**
     * 创建 日志打印策略
     *
     * @param logLevel       日志级别
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
