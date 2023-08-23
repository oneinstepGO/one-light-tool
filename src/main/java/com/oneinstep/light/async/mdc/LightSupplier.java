package com.oneinstep.light.async.mdc;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.function.Supplier;

/**
 * 包含了 reqId 的 Supplier
 *
 * 
 */
@Slf4j
public class LightSupplier<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private final String reqId;
    private static final String REQ_ID_KEY = "reqId";

    public LightSupplier(Supplier<T> supplier, String reqId) {
        this.supplier = supplier;
        this.reqId = reqId;
    }

    @Override
    public T get() {
        try {
            MDC.put(REQ_ID_KEY, reqId);
            log.info("light supplier finished, reqId={}, threadId={}", reqId, Thread.currentThread().getName());
            return supplier.get();
        } finally {
            MDC.clear();
        }
    }

}
