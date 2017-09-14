package cn.didadu;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by zhangjing on 2017/9/14.
 */

@Component
@Scope("prototype")
public class RealServiceCommand extends HystrixCommand<String> {

    protected RealServiceCommand() {
        super(HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RealServiceGroup"))
                .andCommandPropertiesDefaults(
                        // 10s内失败请求数超过1个时熔断器开始生效
                        HystrixCommandProperties.Setter().withCircuitBreakerRequestVolumeThreshold(1)
                )
        );
    }

    @Override
    protected String run() throws Exception {
        System.out.println("Thread:" + Thread.currentThread().getName() + " is handling...");
        throw new RuntimeException();
        //return "Thread:" + Thread.currentThread().getName() + " is handling...";
    }

    @Override
    protected String getFallback() {
        return "Thread:" + Thread.currentThread().getName() + " fallback.";
    }
}
