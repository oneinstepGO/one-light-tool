package com.hst.bss.light.async.mdc;

import org.slf4j.MDC;

/**
 * 包含了 reqId 的 Runnable
 *
 * @author aaron.shaw
 */
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
        } finally {
            MDC.remove(REQ_ID_KEY);
        }
    }

}
