package me.akoala.abc.concurrency.reentrantlock;

import lombok.Data;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 重入锁，手动加锁，释放锁
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/9/10 14:19
 */
public class ReentrantLockTest {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
//        CakeMol cakeMol = new CakeMol(lock);
        ReCakeMol cakeMol = new ReCakeMol(lock);

        Thread t1 = new Thread(cakeMol);
        Thread t2 = new Thread(cakeMol);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("============ finish ");
        System.out.println(cakeMol.getCount());
    }

}

@Data
class CakeMol implements Runnable {

    private int count;
    private ReentrantLock lock;

    public CakeMol(ReentrantLock lock) {
        this.count = 0;
        this.lock = lock;
    }

    @Override
    public void run() {

        for (int i = 0; i < 1000; i++) {
            lock.lock();
            try {
                this.count++;
            } finally {
                lock.unlock();
            }
        }

    }
}

@Data
class ReCakeMol implements Runnable {

    private int count;
    private ReentrantLock lock;

    public ReCakeMol(ReentrantLock lock) {
        this.count = 0;
        this.lock = lock;
    }

    /**
     * <p>
     *     同一线程可以获取同一把锁多次；
     *     但加锁了几次，释放的次数要一样；
     *     少了无法解锁，还继续占有；
     *     多了会抛异常。
     * </p>
     * @author xiaohongxin
     * @param
     * @return
     * @throws
     * @date 14:36 2019/9/10
     **/
    @Override
    public void run() {

        for (int i = 0; i < 1000; i++) {
            lock.lock();
            lock.lock();
            try {
                this.count++;
            } finally {
                lock.unlock();
                lock.unlock();
            }
        }

    }
}