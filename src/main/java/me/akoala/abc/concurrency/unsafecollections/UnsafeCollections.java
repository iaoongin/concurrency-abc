package me.akoala.abc.concurrency.unsafecollections;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Vector;

/**
 * TODO
 *
 * @author xiaohongxin
 * @version 1.0.0
 * @date 2019/8/29 9:57
 */
public class UnsafeCollections {


    public static void main(String[] args) throws InterruptedException {
        testArrayList();
    }

    /**
     * ArrayList出现的结果：
     * 1.19996
     * 2.Exception in thread "Thread-0" java.lang.ArrayIndexOutOfBoundsException: 15
     * 	at java.util.ArrayList.add(ArrayList.java:459)
     * 	at me.akoala.abc.concurrency.unsafecollections.AddToList.run(UnsafeCollections.java:58)
     * 	at java.lang.Thread.run(Thread.java:748)
     * 10008
     * <br/>
     *
     * Vector 出现的结果
     * 20000
     **/
    public static void testArrayList() throws InterruptedException {

        // unsafe
//        List<Object> list = new ArrayList<>();
        // safe
        List<Object> list = new Vector<>();
        Thread t1 = new Thread(new AddToList(list));
        Thread t2 = new Thread(new AddToList(list));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(list.size());
    }

}

@Data
@AllArgsConstructor
class AddToList implements Runnable {

    private List<Object> list;

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
    }
}

