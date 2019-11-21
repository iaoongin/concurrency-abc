package me.akoala.abc.concurrency.scare;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <p>
 * 异步执行，同步返回
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/11/21 17:05
 */
@Slf4j
public class AsyncCallableSyncResultExecutor {


    /*
     * 配置
     */
    /**
     * 线程池大小
     */
    private int poolCoreSize = 4;

    /**
     * 是否需要重试
     */
    @Setter
    private boolean retry;
    @Setter
    private int retryTime = 3;


    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * 任务
     */
    private Map<String, Callable> callableMap;
    private ThreadLocal<Map<String, Callable>> callableMapThreadLocal;

    /**
     * Future 结果
     */
    private Map<String, Future> futureMap;

    /**
     * 结果
     */
    private Map<String, Object> resultMap;

    // 倒数器
    private CountDownLatch latch;

    public AsyncCallableSyncResultExecutor() {
        init();
    }

    public AsyncCallableSyncResultExecutor(int poolCoreSize) {
        this.poolCoreSize = poolCoreSize;
        init();
    }

    private void init() {
        this.callableMap = new HashMap<>();
        this.futureMap = new HashMap<>();
        this.resultMap = new HashMap<>();
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolCoreSize);
    }


    /**
     * 添加任务
     *
     * @param taskId
     * @param callable
     * @return
     */
    public boolean addCallable(String taskId, Callable callable) {
        return this.callableMap.put(taskId, callable) != null;
    }

    /**
     * 执行
     *
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public Map<String, Object> exec(long timeout, TimeUnit unit) throws InterruptedException {

        latch = new CountDownLatch(this.callableMap.size());
        submitTask();

        latch.await(timeout, unit);

        collectResult();

        return this.resultMap;

    }

    /**
     * 执行
     *
     * @return
     * @throws InterruptedException
     */
    public Map<String, Object> exec() throws InterruptedException {

        latch = new CountDownLatch(this.callableMap.size());

        submitTask();

        latch.await();

        collectResult();

        return this.resultMap;

    }

    /**
     * 提交任务，并执行，保存Future
     */
    private void submitTask() {
        for (String key : callableMap.keySet()) {
            Callable callable = callableMap.get(key);
            Callable c = retry ? new AsyncRetryCallable(callable, latch, retryTime) : new AsyncCallable(callable, latch);
            Future future = threadPoolExecutor.submit(c);
            futureMap.put(key, future);
        }
    }


    /**
     * 收集结果
     *
     * @throws InterruptedException
     */
    private void collectResult() throws InterruptedException {
        for (String key : futureMap.keySet()) {
            try {
                this.resultMap.put(key, futureMap.get(key).get());
            } catch (ExecutionException e) {
                log.error(e.getMessage(), e);
                this.resultMap.put(key, null);
            }
        }
    }

    public boolean isRunning() {
        return threadPoolExecutor.getActiveCount() + threadPoolExecutor.getQueue().size() > 0;
    }

    public void clean() {

        if (isRunning()) {
            throw new RuntimeException("pool exists task , please clean force.");
        }

        this.callableMap.clear();

    }
}
