package me.akoala.abc.concurrency.readwritelock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/9/17 9:42
 */
@Slf4j
public class ReadWriteLockDemo {

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    Lock readLock = readWriteLock.readLock();
    Lock writeLock = readWriteLock.writeLock();
    private int sum;

    public int read() {
        readLock.lock();
        try {
            log.info("read sum ={}", this.sum);
            return sum;
        } finally {
            readLock.unlock();
        }
    }

    public int write(int i) {
        writeLock.lock();
        try {
            try {
                log.info("writing ...");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.sum += i;
            log.info("write sum ={}", this.sum);
            return this.sum;
        } finally {
            writeLock.unlock();
        }
    }

    class ReadDemo implements Runnable {

        @Override
        public void run() {
           read();
        }
    }

    class WriteDemo implements Runnable {
        @Override
        public void run() {
            write(1);
        }
    }

    public ReadDemo getReadDemo() {
        return new ReadDemo();
    }

    public WriteDemo getWriteDemo() {
        return new WriteDemo();
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                threadPool.submit(demo.getReadDemo());
            } else {
                threadPool.submit(demo.getWriteDemo());
            }
        }
        Thread.sleep(2000);
        threadPool.shutdown();
//        threadPool.awaitTermination();
    }
}


