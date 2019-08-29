package me.akoala.abc.concurrency.waitnotify;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Future 模拟回调
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/8/28 9:49
 */
@Slf4j
public class WaitNotify {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        GameSystem gameSystem = new GameSystem();
        Future<String> stat = gameSystem.stat(1, 2);
        System.out.println(stat.get());
    }

}


@Slf4j
@Data
class GameSystem {

    private int a;
    private int b;
    // 对象不能修改引用，否则无法通知到
    private final StringBuilder result = new StringBuilder();

    public Future<String> stat(int a, int b) {
        log.info("set future");
        this.a = a;
        this.b = b;
        return new ResultFuture();
    }

    class ResultFuture implements Future<String> {

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public String get() throws InterruptedException, ExecutionException {
            log.info("start future");
            new Compute().start();
            log.info("started future");
            log.info("try wait");
            synchronized (result) {
                result.wait();
            }
            log.info("finish wait");
            return result.toString();
        }

        @Override
        public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

    }

    class Compute extends Thread {

        @Override
        public void run() {

            // 模拟计算时间
            log.info("simulate compute");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getResult().append(getA() + getB());
//            getResult().
            log.info("finish compute");

            log.info("try notify");
            synchronized (result) {
                result.notifyAll();
            }
            log.info("finish notify");
        }
    }
}