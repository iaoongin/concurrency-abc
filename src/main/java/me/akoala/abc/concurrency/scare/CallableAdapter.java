package me.akoala.abc.concurrency.scare;

import java.util.concurrent.Callable;

/**
 * <p>
 * CallableAdapter
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/11/21 17:01
 */
public abstract class CallableAdapter<V> implements Callable<V> {

    private Callable<V> callable;

    public CallableAdapter(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    public V call() throws Exception {
        return this.callable.call();
    }
}
