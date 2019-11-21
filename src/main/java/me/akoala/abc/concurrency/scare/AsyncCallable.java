package me.akoala.abc.concurrency.scare;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * AsyncCallable
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/11/21 17:02
 */
@Slf4j
public class AsyncCallable<V> extends CallableAdapter<V> {

    private CallableStatus status = CallableStatus.INIT;
    private CountDownLatch latch;

    public AsyncCallable(Callable<V> callable, CountDownLatch latch) {
        super(callable);
        this.latch = latch;
    }

    @Override
    public V call() throws Exception {
        status = CallableStatus.CALLING;
        long start = System.currentTimeMillis();
        try {

            V v = super.call();
            status = CallableStatus.SUCCESS;
            return v;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            status = CallableStatus.ERROE;
            return null;
        } finally {
            latch.countDown();
            long end = System.currentTimeMillis();
            log.info("finished in {} ms.", end - start);
        }

    }

    public CallableStatus getStatus() {
        return this.status;
    }
}
