package cn.didadu;

import org.apache.coyote.AbstractProtocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

/**
 * Created by zhangjing on 2017/8/25.
 */
@SpringBootApplication
public class RealServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealServerApplication.class, args);
    }
}
