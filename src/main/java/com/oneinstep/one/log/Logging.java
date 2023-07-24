package com.hst.bss.light.log.annotation;

import com.hst.bss.light.log.enums.PrintLogLevel;

import java.lang.annotation.*;

import static com.hst.bss.light.log.enums.PrintLogLevel.INFO;

/**
 * 打印方法参数、返回值、异常的日志注解
 * 注意，需要Spring 管理的Bean 才能使用，静态类，自己new 的对象无法使用
 *
 * @author aaron.shaw
 * @date 2023-03-24 16:16
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logging {

    /**
     * 是否打印 参数
     *
     * @return 是否打印 参数
     */
    boolean printArgs() default false;

    /**
     * 是否打印返回值
     *
     * @return 是否打印返回值
     */
    boolean printResult() default false;

    /**
     * 是否打印异常
     * <p> 建议只在最外层打印异常，避免重复打印 </p>
     *
     * @return 是否打印异常
     */
    boolean printError() default false;

    /**
     * 日志切面所使用的日志级别，异常依旧会用 log.error() 打印。
     * 默认为 INFO 级别。
     */
    PrintLogLevel logLevel() default INFO;

}
