package com.oneinstep.light.async.mdc;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.MDC;

import java.util.function.Supplier;

/**
 * MDC上下文
 * <p>
 * 线程池穿透
 * ExecutorService oriExecutorService = new ThreadPoolExecutor(
 * 10,
 * 10,
 * 60,
 * TimeUnit.SECONDS,
 * new LinkedBlockingQueue<>(10),
 * new ThreadFactoryBuilder().setNameFormat("your-thread-pool-%d").build(),
 * new ThreadPoolExecutor.AbortPolicy());
 * <p>
 * // 用TtlExecutors装饰线程池
 * this.executorService = TtlExecutors.getTtlExecutorService(oriExecutorService);
 * <p>
 * // 使用 AsyncMdcContext.doRunnableWithReqId() or AsyncMdcContext.doSupplierWithReqId()
 * AsyncMdcContext.doRunnableWithReqId(() -> {
 * for (int i = 0; i < 10; i++) {
 * int finalI = i;
 * executorService.execute(new LightRunnable(() -> {
 * // 业务代码
 * // 此时 MDC 就会有 reqId
 * log.info("i = {}, reqId from MDC: {}", finalI, MDC.get(MdcContext.REQ_ID_KEY));
 * }, AsyncMdcContext.getReqId()));
 * }
 * });
 *
 * @since 1.1.1
 */
public class AsyncMdcContext {

    public static final String REQ_ID_KEY = "reqId";

    /**
     * 保存 reqId
     * 用于线程穿透
     */
    private final TransmittableThreadLocal<String> reqIdThreadLocal;

    public AsyncMdcContext() {
        this.reqIdThreadLocal = new TransmittableThreadLocal<>();
        this.reqIdThreadLocal.set(MDC.get(REQ_ID_KEY));
    }

    public String getReqId() {
        return this.reqIdThreadLocal.get();
    }

    /**
     * 执行 runnable
     *
     * @param runnable runnable
     */
    public void doRunnableWithSameReqId(Runnable runnable) {
        try {
            runnable.run();
        } finally {
            this.reqIdThreadLocal.remove();
        }
    }

    /**
     * 执行 supplier
     *
     * @param supplier supplier
     * @param <T>      T
     * @return T
     */
    public <T> T doSupplierWithSameReqId(Supplier<T> supplier) {
        try {
            return supplier.get();
        } finally {
            this.reqIdThreadLocal.remove();
        }
    }

}
