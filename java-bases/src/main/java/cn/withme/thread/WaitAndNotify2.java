/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月23日
 */
package cn.withme.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: WaitAndNotify2
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月23日
 */
public class WaitAndNotify2 {


    private static volatile AtomicInteger number = new AtomicInteger(0);

    private static volatile BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(1, true);

    public static void provider() {
        while (true) {
            int numb = number.incrementAndGet();
            System.out.println(Thread.currentThread().getName() + "生成了数字:" + numb);
            try {
                blockingQueue.put(numb);
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    public static void consumer() {
        while (true) {
            Integer getNumber = null;
            try {
                getNumber = blockingQueue.take();
                System.out.println(Thread.currentThread().getName() + "消费了数字:" + getNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("demo-cn.withme.thread-%d").build();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 3, 50, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), factory);
        poolExecutor.execute(() -> consumer());
        poolExecutor.execute(() -> provider());
    }

}
