package me.akoala.abc.concurrency.reentrantlock;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 可中断锁,某线程在等待锁的时候可以中断等待
 * </p>
 * <p>
 * <p>步骤说明：
 * <p>lemon->get lock1,strawberry->get lock2;
 * <p>lemon->get interrupt;
 * <p>lemon->unlock lock1,lemon->finished;
 * <p>strawberry->get lock1,strawberry->run out->finished;
 *
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/9/11 9:56
 */
@Slf4j
public class LockInterruptiblyTest {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();
        Lemon lemon = new Lemon(lock1, lock2, 0);
        Lemon strawberry = new Lemon(lock1, lock2, 1);
        Thread lemonT = new Thread(lemon, "lemon");
        Thread strawberryT = new Thread(strawberry, "strawberry");

        lemonT.start();
        strawberryT.start();

        Thread.sleep(1000);
        log.info("{}", "main sleep over");

        log.info("{}", "interrupt strawberry...");
        strawberryT.interrupt();

        lemonT.join();
        strawberryT.join();
        log.info("{}", "main done");
    }

}

@AllArgsConstructor
@Slf4j
class Lemon implements Runnable {

    private ReentrantLock lock1;
    private ReentrantLock lock2;
    private int intLock;    // 用于选择获取锁顺序


    @Override
    public void run() {

        try {

            if (intLock == 1) {
                lock1.lockInterruptibly();
                log.info("lock1: {}", lock1.isHeldByCurrentThread());
                Thread.sleep(300);
                lock2.lockInterruptibly();
                log.info("lock2: {}", lock2.isHeldByCurrentThread());
                log.info("{}: run out", Thread.currentThread().getName());
            } else {
                lock2.lockInterruptibly();
                log.info("lock1: {}", lock2.isHeldByCurrentThread());
                Thread.sleep(300);
                lock1.lockInterruptibly();
                log.info("lock2: {}", lock1.isHeldByCurrentThread());
                log.info("{}: run out", Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock1.isHeldByCurrentThread()) {
                lock1.unlock();
            }
            if (lock2.isHeldByCurrentThread()) {
                lock2.unlock();
            }
        }


    }
}