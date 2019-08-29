package me.akoala.abc.concurrency.deamon;

/**
 * TODO
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/8/28 11:30
 */
public class Deamon {

    public static void main(String[] args) {

        DeamonThread deamonThread = new DeamonThread();

        // ======= 如果不设置为守护线程，即作为用户线程，会一直执行下去
        // deamonThread.setDaemon(false);

        // ======= 如果设置为守护线程，在main （用户线程）执行完，因没有守护的对象，会退出jvm
        deamonThread.setDaemon(true);
        deamonThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class DeamonThread extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println(1);
        }
    }
}