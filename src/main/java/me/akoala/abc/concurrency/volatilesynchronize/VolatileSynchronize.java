package me.akoala.abc.concurrency.volatilesynchronize;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Volatile 和 Synchronize
 * Volatile 能避免指令重排，保证一定可见性，但不完全可靠
 * Synchronize 直接对对象堆进行加锁，但注意对象不能变，否则加锁无效
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/8/29 9:36
 */

@Slf4j

public class VolatileSynchronize {

    public static void main(String[] args) throws InterruptedException {
//        VolatileBase aVolatile = new Volatile1();
//        VolatileBase aVolatile = new Volatile2();
        VolatileBase aVolatile = new Volatile3();
        Thread t1 = new Thread(aVolatile, "乌龟");
        Thread t2 = new Thread(aVolatile, "兔子");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.info("i={}", aVolatile.getI());
    }
}

abstract class VolatileBase implements Runnable {
    abstract int getI();
}

/**
 * 无volatile，无synchronize
 *
 * @param
 * @author xiaohongxin
 * @return
 * @throws
 * @date 9:46 2019/8/29
 **/
@Data
class Volatile1 extends VolatileBase {

    int i = 0;

    @Override
    public void run() {

        for (int j = 0; j < 100000; j++) {
            i++;
        }

    }
}

/**
 * 有volatile，无synchronize
 *
 * @param
 * @author xiaohongxin
 * @return
 * @throws
 * @date 9:47 2019/8/29
 **/
@Data
class Volatile2 extends VolatileBase {

    volatile int i = 0;

    @Override
    public void run() {

        for (int j = 0; j < 100000; j++) {
            i++;
        }

    }
}

/**
 * 无volatile，有synchronize
 *
 * @param
 * @author xiaohongxin
 * @return
 * @throws
 * @date 9:47 2019/8/29
 **/
@Data
class Volatile3 extends VolatileBase {

    int i = 0;

    @Override
    public synchronized void run() {

        for (int j = 0; j < 100000; j++) {
            i++;
        }

    }
}