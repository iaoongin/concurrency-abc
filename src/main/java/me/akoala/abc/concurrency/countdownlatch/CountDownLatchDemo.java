package me.akoala.abc.concurrency.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * 倒数计数器
 * </p>
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/9/17 11:15
 */
@Slf4j
public class CountDownLatchDemo implements Runnable {

    private CountDownLatch latch = new CountDownLatch(10);

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(100));
            log.info("{}, check done ...", latch.getCount());
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CountDownLatchDemo demo = new CountDownLatchDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(demo).start();
        }
        try {
            demo.latch.await();
            log.info("fire ...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
