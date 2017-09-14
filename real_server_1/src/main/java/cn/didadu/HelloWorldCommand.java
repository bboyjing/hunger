package cn.didadu;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;

import java.util.concurrent.Future;

/**
 * Created by zhangjing on 2017/9/14.
 */
public class HelloWorldCommand extends HystrixCommand<String> {

    private final String name;

    public HelloWorldCommand(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        return "Hello " + name +" thread:" + Thread.currentThread().getName();
    }

    public static void main(String[] args) throws Exception {
        /**
         * Command对象的使用有如下三种方式
         * 注：每个Command对象不是单例的，不能重用
         */

        // 同步调用
        HelloWorldCommand helloWorldCommand = new HelloWorldCommand("Synchronous-hystrix");
        System.out.println("result=" + helloWorldCommand.execute());

        // Future方式的异步调用
        helloWorldCommand = new HelloWorldCommand("Asynchronous-hystrix");
        Future<String> future = helloWorldCommand.queue();
        System.out.println("result=" + future.get());

        // RxJava响应式
        helloWorldCommand = new HelloWorldCommand("RxJava-hystrix");
        Observable<String> observable = helloWorldCommand.observe();
        observable.asObservable().subscribe(result -> System.out.println("result=" + result));

        System.out.println("mainThread=" + Thread.currentThread().getName());
    }
}
