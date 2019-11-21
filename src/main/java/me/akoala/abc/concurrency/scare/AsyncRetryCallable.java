package me.akoala.abc.concurrency.scare;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * AsyncRetryCallable
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/11/21 17:02
 */
@Slf4j
public class AsyncRetryCallable<V> extends CallableAdapter<V> {

    private CallableStatus status = CallableStatus.INIT;
    private int retryTimes = 3;
    private CountDownLatch latch;

    public AsyncRetryCallable(Callable<V> callable, CountDownLatch latch, int retryTimes) {
        super(callable);
        this.latch = latch;
        this.retryTimes = retryTimes;
    }

    @Override
    public V call() {
        long start = System.currentTimeMillis();

        // 主要的 start
        V call = call(0);
        latch.countDown();
        // 主要的 end


        long end = System.currentTimeMillis();
        log.info("finished in {} ms.", end - start);
        return call;

    }

    private V call(int errorTimes) {
        status = CallableStatus.CALLING;

        try {
            V v = super.call();
            status = CallableStatus.SUCCESS;
            return v;
        } catch (Exception e) {
            errorTimes++;
            log.warn("retry times {}.", errorTimes);
            if (errorTimes < retryTimes) {
                return call(errorTimes);
            } else {
                log.error("retry end.");
                status = CallableStatus.ERROE;
                return null;
            }
        }
    }

    public CallableStatus getStatus() {
        return this.status;
    }
}
