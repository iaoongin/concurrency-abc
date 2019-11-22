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
    private ThreadLocal<Map<String, Callable>> callableMapThreadLocal;

    /**
     * Future 结果
     */
    private ThreadLocal<Map<String, Future>> futureMapThreadLocal;

    /**
     * 结果
     */
    private ThreadLocal<Map<String, Object>> resultMapThreadLocal;

    // 倒数器
    private ThreadLocal<CountDownLatch> latchThreadLocal;

    public AsyncCallableSyncResultExecutor() {
        init();
    }

    public AsyncCallableSyncResultExecutor(int poolCoreSize) {
        this.poolCoreSize = poolCoreSize;
        init();
    }

    private void init() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolCoreSize);
        this.callableMapThreadLocal = new ThreadLocal<>();
        this.futureMapThreadLocal = new ThreadLocal<>();
        this.resultMapThreadLocal = new ThreadLocal<>();
        this.latchThreadLocal = new ThreadLocal<>();
    }


    private Map<String, Callable> getCallableMap() {
        if (this.callableMapThreadLocal.get() == null) {
            this.callableMapThreadLocal.set(new HashMap<>());
        }
        return this.callableMapThreadLocal.get();
    }

    private Map<String, Future> getFutureMap() {
        if (this.futureMapThreadLocal.get() == null) {
            this.futureMapThreadLocal.set(new HashMap<>());
        }
        return this.futureMapThreadLocal.get();
    }

    private Map<String, Object> getResultMap() {
        if (this.resultMapThreadLocal.get() == null) {
            this.resultMapThreadLocal.set(new HashMap<>());
        }
        return this.resultMapThreadLocal.get();
    }

    private CountDownLatch getLatch() {
        return this.latchThreadLocal.get();
    }

    private void setLatch(int size) {
        this.latchThreadLocal.set(new CountDownLatch(size));
    }

    /**
     * 添加任务
     *
     * @param taskId
     * @param callable
     * @return
     */
    public boolean addCallable(String taskId, Callable callable) {
        return this.getCallableMap().put(taskId, callable) != null;
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

        setLatch(this.callableMapThreadLocal.get().size());

        submitTask();

        getLatch().await(timeout, unit);

        collectResult();

        Map<String, Object> result = this.resultMapThreadLocal.get();

        clean();

        return result;

    }

    /**
     * 执行
     *
     * @return
     * @throws InterruptedException
     */
    public Map<String, Object> exec() throws InterruptedException {

        setLatch(this.callableMapThreadLocal.get().size());

        submitTask();

        getLatch().await();

        collectResult();

        Map<String, Object> result = this.resultMapThreadLocal.get();

        clean();

        return result;

    }


    /**
     * 提交任务，并执行，保存Future
     */
    private void submitTask() {
        for (String key : getCallableMap().keySet()) {
            Callable callable = getCallableMap().get(key);
            Callable c = retry ? new AsyncRetryCallable(callable, getLatch(), retryTime) : new AsyncCallable(callable, getLatch());
            Future future = threadPoolExecutor.submit(c);
            getFutureMap().put(key, future);
        }
    }


    /**
     * 收集结果
     *
     * @throws InterruptedException
     */
    private void collectResult() throws InterruptedException {
        for (String key : getFutureMap().keySet()) {
            try {
                this.getResultMap().put(key, this.getFutureMap().get(key).get());
            } catch (ExecutionException e) {
                log.error(e.getMessage(), e);
                this.getResultMap().put(key, null);
            }
        }
    }

    public boolean isRunning() {
        return threadPoolExecutor.getActiveCount() + threadPoolExecutor.getQueue().size() > 0;
    }

    public void clean() {

        log.info("clean threadLocal.");

        this.callableMapThreadLocal.remove();
        this.futureMapThreadLocal.remove();
        this.resultMapThreadLocal.remove();
        this.latchThreadLocal.remove();

    }

}
