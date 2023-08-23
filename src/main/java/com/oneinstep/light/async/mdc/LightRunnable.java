package com.oneinstep.light.async.mdc;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * 包含了 reqId 的 Runnable
 */
@Slf4j
public class LightRunnable implements Runnable {

    private final Runnable runnable;
    private final String reqId;
    private static final String REQ_ID_KEY = "reqId";

    public LightRunnable(Runnable runnable, String reqId) {
        this.runnable = runnable;
        this.reqId = reqId;
    }

    @Override
    public void run() {
        try {
            MDC.put(REQ_ID_KEY, reqId);
            runnable.run();
            log.info("light runnable finished, reqId={}, threadId={}", reqId, Thread.currentThread().getName());
        } finally {
            MDC.clear();
        }
    }

}
