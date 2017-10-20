package cn.didadu;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangjing on 2017/10/10.
 */
public class RateLimiterTest {
    public static void main(String[] args) throws InterruptedException {
        //test1();
        //test2();
        //test3();
        test4();
    }

    private static void test1() {
        // 表示桶容量为5，且每秒新增5个令牌，即每格200毫秒新增一个令牌
        RateLimiter limiter = RateLimiter.create(5);
        /**
         * acquire()表示消费一个令牌
         * 如果当前桶中有足够令牌，则成功(返回0)
         * 如果桶中没有令牌，则等待一段时间再去消费令牌
         */
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
    }

    private static void test2() {
        // 令牌桶算法允许一定程度的突发，所以可以一次性消费5个令牌
        RateLimiter limiter = RateLimiter.create(5);
        System.out.println(limiter.acquire(5));
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));
    }

    private static void test3() throws InterruptedException {
        RateLimiter limiter = RateLimiter.create(2);
        System.out.println(limiter.acquire());
        Thread.sleep(2000l);
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());

        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
    }

    private static void test4() throws InterruptedException {
        RateLimiter limiter = RateLimiter.create(5, 1000, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.acquire());
        }
        Thread.sleep(1000l);
        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.acquire());
        }
    }
}
