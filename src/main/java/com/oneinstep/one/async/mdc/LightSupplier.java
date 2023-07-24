package com.hst.bss.light.async.mdc;

import org.slf4j.MDC;

import java.util.function.Supplier;

/**
 * 包含了 reqId 的 Supplier
 *
 * @author aaron.shaw
 */
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
            return supplier.get();
        } finally {
            MDC.remove(REQ_ID_KEY);
        }
    }

}
