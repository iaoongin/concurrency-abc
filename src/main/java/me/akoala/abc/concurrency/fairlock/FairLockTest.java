package me.akoala.abc.concurrency.fairlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 公平锁测试
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/9/16 14:32
 */
@Slf4j
public class FairLockTest implements Runnable {

    /**
     * <p>通过改变构造参数，观察结果
     * <p>公平锁会使得线程偏向交替运行</p>
     * <p>非公平锁会使得线程偏向上次获得锁的线程运行</p>
     */
    private ReentrantLock fairLock = new ReentrantLock(true);
//    private ReentrantLock fairLock = new ReentrantLock(false);

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {

            fairLock.lock();
            log.info("{}-获得锁", Thread.currentThread().getName());
            fairLock.unlock();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        FairLockTest fairLockTest = new FairLockTest();
        Thread cloud = new Thread(fairLockTest, "cloud");
        Thread stream = new Thread(fairLockTest, "stream");

        cloud.start();
        stream.start();

        cloud.join();
        stream.join();

    }
}
