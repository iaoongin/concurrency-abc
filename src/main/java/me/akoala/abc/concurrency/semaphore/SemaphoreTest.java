package me.akoala.abc.concurrency.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * <p>
 * 信号量-允许多个线程访问
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/9/16 17:26
 */
@Slf4j
public class SemaphoreTest implements Runnable {

    private Semaphore semaphore = new Semaphore(5);


    @Override
    public void run() {
        try {
            semaphore.acquire();
            Thread.sleep(2000);
            log.info("six six");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        SemaphoreTest test = new SemaphoreTest();
        for (int i = 0; i < 20; i++) {
            threadPool.submit(test);
        }
        Thread.sleep(10000);
        threadPool.shutdown();

    }
}
