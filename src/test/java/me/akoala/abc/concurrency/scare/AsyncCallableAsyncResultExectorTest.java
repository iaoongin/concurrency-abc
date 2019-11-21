package me.akoala.abc.concurrency.scare;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AsyncCallableAsyncResultExectorTest {

    @org.junit.Test
    public void exec() throws InterruptedException {

        long start = System.currentTimeMillis();
        AsyncCallableSyncResultExecutor exector = new AsyncCallableSyncResultExecutor();
        for (int i = 1; i <= 3; i++) {
            String s = "" + i;
            final int t = i;
            Callable<String> c = () -> {
                TimeUnit.SECONDS.sleep(t);
                return s;
            };

            exector.addCallable(s, c);
        }

        Map<String, Object> exec = exector.exec();
//        Map<String, Object> exec2 = exector.exec();
        long end = System.currentTimeMillis();

        log.info("main result {}", exec);
//        log.info("main result {}", exec2);
        log.info("main finished in {} ms", end - start);


    }

    @org.junit.Test
    public void exec2() throws InterruptedException {

        long start = System.currentTimeMillis();
        AsyncCallableSyncResultExecutor exector = new AsyncCallableSyncResultExecutor();
        for (int i = 1; i <= 3; i++) {
            String s = "" + i;
            final int t = i;
            Callable<String> c = () -> {
                TimeUnit.SECONDS.sleep(t);
                if(t == 2){
                    throw new Exception("error. t = 2.");
                }
                return s;
            };

            exector.addCallable(s, c);
        }

        Map<String, Object> exec = exector.exec();
//        Map<String, Object> exec2 = exector.exec();
        long end = System.currentTimeMillis();

        log.info("main result {}", exec);
//        log.info("main result {}", exec2);
        log.info("main finished in {} ms", end - start);


    }

    @org.junit.Test
    public void exec3() throws InterruptedException {

        long start = System.currentTimeMillis();
        AsyncCallableSyncResultExecutor exector = new AsyncCallableSyncResultExecutor();
        for (int i = 1; i <= 3; i++) {
            String s = "" + i;
            final int t = i;
            Callable<String> c = () -> {
                TimeUnit.SECONDS.sleep(t);
                if(t == 2){
                    throw new Exception("error. t = 2.");
                }
                return s;
            };

            exector.addCallable(s, c);
        }

        exector.setRetry(true);
        exector.setRetryTime(4);
        Map<String, Object> exec = exector.exec();
//        Map<String, Object> exec2 = exector.exec();
        long end = System.currentTimeMillis();

        log.info("main result {}", exec);
//        log.info("main result {}", exec2);
        log.info("main finished in {} ms", end - start);


    }
}
