package me.akoala.abc.concurrency.scare;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AsyncCallableAsyncResultExectorTest {

    @org.junit.Test
    public void exec() throws InterruptedException {

        long start = System.currentTimeMillis();
        AsyncCallableSyncResultExecutor executor = new AsyncCallableSyncResultExecutor();
        for (int i = 1; i <= 3; i++) {
            String s = "" + i;
            final int t = i;
            Callable<String> c = () -> {
                TimeUnit.SECONDS.sleep(t);
                return s;
            };

            executor.addCallable(s, c);
        }

        Map<String, Object> exec = executor.exec();
//        Map<String, Object> exec2 = executor.exec();
        long end = System.currentTimeMillis();

        log.info("main result {}", exec);
//        log.info("main result {}", exec2);
        log.info("main finished in {} ms", end - start);


    }

    @org.junit.Test
    public void exec2() throws InterruptedException {

        long start = System.currentTimeMillis();
        AsyncCallableSyncResultExecutor executor = new AsyncCallableSyncResultExecutor();
        for (int i = 1; i <= 3; i++) {
            String s = "" + i;
            final int t = i;
            Callable<String> c = () -> {
                TimeUnit.SECONDS.sleep(t);
                if (t == 2) {
                    throw new Exception("error. t = 2.");
                }
                return s;
            };

            executor.addCallable(s, c);
        }

        Map<String, Object> exec = executor.exec();
//        Map<String, Object> exec2 = executor.exec();
        long end = System.currentTimeMillis();

        log.info("main result {}", exec);
//        log.info("main result {}", exec2);
        log.info("main finished in {} ms", end - start);


    }

    @org.junit.Test
    public void exec3() throws InterruptedException {

        long start = System.currentTimeMillis();
        AsyncCallableSyncResultExecutor executor = new AsyncCallableSyncResultExecutor();
        for (int i = 1; i <= 3; i++) {
            String s = "" + i;
            final int t = i;
            Callable<String> c = () -> {
                TimeUnit.SECONDS.sleep(t);
                if (t == 2) {
                    throw new Exception("error. t = 2.");
                }
                return s;
            };

            executor.addCallable(s, c);
        }

        executor.setRetry(true);
        executor.setRetryTime(4);
        Map<String, Object> exec = executor.exec();
//        Map<String, Object> exec2 = executor.exec();
        long end = System.currentTimeMillis();

        log.info("main result {}", exec);
//        log.info("main result {}", exec2);
        log.info("main finished in {} ms", end - start);


    }

    @org.junit.Test
    public void exec4() {

        AsyncCallableSyncResultExecutor executor = new AsyncCallableSyncResultExecutor();
        for (int j = 0; j < 2; j++) {
            int finalJ = j;
            new Thread(() -> {
                long start = System.currentTimeMillis();
                for (int i = 1; i <= 3; i++) {
                    String s = "" + finalJ + i;
                    final int finalI = i;
                    Callable<String> c = () -> {
                        TimeUnit.SECONDS.sleep(finalI + finalJ);
                        if (finalI == 2) {
                            throw new Exception("error. finalI = 2.");
                        }
                        return s;
                    };

                    executor.addCallable(s, c);
                }

                executor.setRetry(true);
                executor.setRetryTime(4);
                Map<String, Object> exec = null;
                try {
                    exec = executor.exec();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long end = System.currentTimeMillis();

                log.info("{} result {}", Thread.currentThread(), exec);
                log.info("{} finished in {} ms", Thread.currentThread(), end - start);
            }).start();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
