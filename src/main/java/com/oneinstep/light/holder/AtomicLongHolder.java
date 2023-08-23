package com.oneinstep.light.holder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 保存 long 值的 ThreadLocal
 */
public class AtomicLongHolder {

    private final AtomicLong value;

    /**
     * 构造方法
     */
    public AtomicLongHolder() {
        value = new AtomicLong(0);
    }

    /**
     * 获取值
     * 每次自增
     *
     * @return 自增的 long 值
     */
    public long getAndIncrement() {
        return value.getAndIncrement();
    }

}
