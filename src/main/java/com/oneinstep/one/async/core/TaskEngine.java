package com.hst.bss.light.async.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hst.arch.components.exception.AbstractHSTBaseException;
import com.hst.arch.components.exception.types.HSTAlertingBaseException;
import com.hst.arch.components.exception.types.HSTPromptingBaseException;
import com.hst.bss.light.async.config.TaskConfig;
import com.hst.bss.light.async.context.AsyncTaskContext;
import com.hst.bss.light.log.enums.PrintLogLevel;
import com.hst.bss.light.log.strategy.LogPrintStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.hst.bss.light.log.factory.LogPrintStrategyFactory.createLogPrintStrategy;

/**
 * 任务引擎
 *
 * <pre>
 *
 * // 创建异步任务执行上下文
 * AsyncTaskContext asyncTaskContext = buildTaskContext(option, holdingCommonOperationBO);
 * // 获取任务执行引擎单例
 * TaskEngine taskEngine = TaskEngine.getInstance();
 * // 启动任务引擎，传入线程池，若不传入线程池，将使用一个框架创建的默认线程池
 * taskEngine.startEngine(asyncTaskContext, executorService);
 * // 从 AsyncTaskContext 可获取所有任务执行结果
 * Map<String, BaseResponseDTO<?>> taskResultMap = asyncTaskContext.getTaskResultMap();
 * // 获取某个任务执行结果
 * BaseResponseDTO<?> yourTaskResult = taskResultMap.get("yourTaskId");
 * YourDataType data = (YourDataType) yourTaskResult.getData();
 * </pre>
 *
 * @author aaron.shaw
 */
@Slf4j
public class TaskEngine {

    private static final String REQ_ID_KEY = "reqId";

    private final ExecutorService defaultThreadPool;

    private TaskEngine() {
        ExecutorService executorService = new ThreadPoolExecutor(
                100,
                100,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(128),
                new ThreadFactoryBuilder().setNameFormat("DEFAULT-LIGHT-ASYNC-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());

        // 用TtlExecutors装饰线程池
        this.defaultThreadPool = TtlExecutors.getTtlExecutorService(executorService);
    }

    private static class TaskEngineHolder {
        /**
         * 任务执行引擎 单例
         */
        private static final TaskEngine INSTANCE = new TaskEngine();
    }

    /**
     * 获取任务执行引擎单例
     *
     * @return 任务执行引擎
     */

    public static TaskEngine getInstance() {
        return TaskEngineHolder.INSTANCE;
    }

    /**
     * 运行时 task 类缓存，避免重复反射创建
     */
    private static final Map<String, AbstractTask<?>> RUN_TIME_TASK_MAP = new ConcurrentHashMap<>();

    /**
     * 使用TransmittableThreadLocal
     */
    private static final TransmittableThreadLocal<String> REQ_ID_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 启动任务引擎
     *
     * @param context         请求上下文
     * @param executorService 任务执行使用的线程池，若不传入线程池，将使用默认的线程池
     */
    public void startEngine(@Valid @NotNull AsyncTaskContext context, ExecutorService executorService) {

        PrintLogLevel taskLogLevel = context.getLogLevel();
        LogPrintStrategy logPrintStrategy = createLogPrintStrategy(taskLogLevel);

        // 设置线程池
        ExecutorService usedThreadPool;
        if (executorService != null) {
            usedThreadPool = TtlExecutors.getTtlExecutorService(executorService);
            logPrintStrategy.printLog(log, "Use the default thread pool.");
        } else {
            usedThreadPool = this.defaultThreadPool;
            logPrintStrategy.printLog(log, "Use the user set thread pool.");
        }

        try {
            REQ_ID_THREAD_LOCAL.set(MDC.get(REQ_ID_KEY));
            TaskConfig taskConfig = context.getTaskConfig();
            List<List<String>> arrangeRules = taskConfig.getArrangeRules();

            Map<String, String> taskIdAndClassNameMap = taskConfig.getTaskDetailsMap().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getFullClassName()));
            try {
                arrangeAndExecAsync(context, usedThreadPool, arrangeRules, taskIdAndClassNameMap, logPrintStrategy);
            } catch (CompletionException completionException) {
                Throwable cause = completionException.getCause();
                if (cause instanceof AbstractHSTBaseException) {
                    throw (AbstractHSTBaseException) cause;
                }
                throw completionException;
            }
        } finally {
            logPrintStrategy.printLog(log, String.format("clear the reqIdThreadLocal ==> %s", REQ_ID_THREAD_LOCAL.get()));
            REQ_ID_THREAD_LOCAL.remove();
        }
    }

    private void arrangeAndExecAsync(AsyncTaskContext context, ExecutorService usedThreadPool, List<List<String>> arrangeRules, Map<String, String> taskIdAndClassNameMap, LogPrintStrategy logPrintStrategy) {
        // 一行为一个编排组 保存编排组
        List<CompletableFuture<Void>> commonFutures = new ArrayList<>();
        for (int i = 0; i < arrangeRules.size(); i++) {
            List<String> arrangeRuleLine = arrangeRules.get(i);
            if (i == 0) {
                arrangeGroup(arrangeRuleLine, context, taskIdAndClassNameMap, usedThreadPool, logPrintStrategy).join();
            } else if (i == arrangeRules.size() - 1) {
                CompletableFuture.allOf(commonFutures.toArray(new CompletableFuture[0])).join();
                arrangeGroup(arrangeRuleLine, context, taskIdAndClassNameMap, usedThreadPool, logPrintStrategy).join();
            } else {
                commonFutures.add(arrangeGroup(arrangeRuleLine, context, taskIdAndClassNameMap, usedThreadPool, logPrintStrategy));
            }
        }
    }

    private List<AbstractTask<?>> getTasks(List<String> taskIdsArr, Map<String, String> taskClassNameMap, LogPrintStrategy logPrintStrategy) {
        return taskIdsArr.stream().map(taskId -> {
            AbstractTask<?> task = RUN_TIME_TASK_MAP.get(taskId);

            String taskClassName = taskClassNameMap.get(taskId);
            if (taskClassName != null) {
                taskClassName = taskClassName.trim();
            }
            if (task != null && task.getClass().getSimpleName().equalsIgnoreCase(taskClassName)) {
                logPrintStrategy.printLog(log, String.format("Get the task, TaskId: %s, TaskName:%s", task.getTaskId(), task.getClass().getSimpleName()));
                return task;
            }
            try {
                if (StringUtils.isBlank(taskClassName)) {
                    log.error("There is no task class name in the taskClassNameMap.");
                    throw new HSTPromptingBaseException("bss.light.wrong.taskClassName", "wrong taskClassName");
                }
                task = (AbstractTask<?>) Class.forName(taskClassName).getConstructor().newInstance();
                task.setTaskId(taskId);

                logPrintStrategy.printLog(log, String.format("Create the new Task, TaskId: %s, TaskName: %s", task.getTaskId(), task.getClass().getSimpleName()));

                RUN_TIME_TASK_MAP.put(taskId, task);
                return task;
            } catch (Exception e) {
                log.error("Instantiation Exception during taskId={}, taskName={}", taskId, taskClassName, e);
                throw new HSTAlertingBaseException("bss.light.instantiation.task.error", "CREATE TASK: " + taskClassName + " ERROR");
            }
        }).collect(Collectors.toList());
    }

    /**
     * 编排任务
     *
     * @param arrange          编排行
     * @param taskContext      任务上下文
     * @param taskNameMap      任务详情map
     * @param usedThreadPool   使用的线程池
     * @param logPrintStrategy 日志打印器
     * @return CompletableFuture
     */
    private CompletableFuture<Void> arrangeGroup(List<String> arrange, AsyncTaskContext taskContext, Map<String, String> taskNameMap, @NotNull ExecutorService usedThreadPool, LogPrintStrategy logPrintStrategy) {
        // 映射 taskId -> future 存储Future
        Map<String, CompletableFuture<Void>> alreadySubmitFutureMap = new ConcurrentHashMap<>(16);
        // 存储所有的 CompletableFuture
        List<CompletableFuture<Void>> allFutures = new ArrayList<>(16);

        for (int i = 0; i < arrange.size(); i++) {
            String arrangeLine = arrange.get(i);
            String[] arrangeSegArr = arrangeLine.split(":");
            List<String> fatherTaskIds = Arrays.asList(arrangeSegArr[0].split(","));
            List<AbstractTask<?>> fatherTasks = getTasks(fatherTaskIds, taskNameMap, logPrintStrategy);

            if (arrange.size() == 1) {
                // 第一行，直接存入 map
                fatherTasks.forEach(fatherTask -> {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> addReqIdAndAccept(taskContext, fatherTask), usedThreadPool);
                    allFutures.add(future);
                    alreadySubmitFutureMap.put(fatherTask.getTaskId(), future);
                });

                if (arrangeSegArr.length > 1) {
                    List<String> secondTaskIds = Collections.singletonList(arrangeSegArr[1]);
                    List<AbstractTask<?>> secondTasks = getTasks(secondTaskIds, taskNameMap, logPrintStrategy);
                    AbstractTask<?> lineEndTask = secondTasks.get(0);
                    alreadySubmitFutureMap.clear();
                    return CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).thenAcceptAsync(r -> addReqIdAndAccept(taskContext, lineEndTask), usedThreadPool);
                }

                alreadySubmitFutureMap.clear();
                return CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]));
            }

            if (i == 0) {
                // 第一行，直接存入 map
                fatherTasks.forEach(fatherTask -> {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> addReqIdAndAccept(taskContext, fatherTask), usedThreadPool);
                    allFutures.add(future);
                    alreadySubmitFutureMap.put(fatherTask.getTaskId(), future);
                });
            } else {
                CompletableFuture<Void> lineFuture = CompletableFuture.allOf(
                        fatherTasks.stream()
                                .map(fatherTask -> Optional.ofNullable(alreadySubmitFutureMap.get(fatherTask.getTaskId()))
                                        .orElseThrow(() -> new RuntimeException("future not in start futures.")))
                                .toArray(CompletableFuture[]::new)
                );

                if (arrangeSegArr.length > 1) {
                    List<String> secondTaskIds = Collections.singletonList(arrangeSegArr[1]);
                    List<AbstractTask<?>> secondTasks = getTasks(secondTaskIds, taskNameMap, logPrintStrategy);
                    AbstractTask<?> lineEndTask = secondTasks.get(0);
                    lineFuture = lineFuture.thenAcceptAsync(r -> addReqIdAndAccept(taskContext, lineEndTask), usedThreadPool);
                    alreadySubmitFutureMap.put(lineEndTask.getTaskId(), lineFuture);
                }

                allFutures.add(lineFuture);
            }
        }

        CompletableFuture<Void> wholeFuture = CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]));
        alreadySubmitFutureMap.clear();
        return wholeFuture;
    }

    private void addReqIdAndAccept(AsyncTaskContext taskContext, AbstractTask<?> task) {
        MDC.put(REQ_ID_KEY, REQ_ID_THREAD_LOCAL.get());
        task.accept(taskContext);
    }

}
