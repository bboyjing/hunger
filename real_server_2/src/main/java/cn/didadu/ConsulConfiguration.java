package cn.didadu;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by zhangjing on 2017/9/5.
 */

@Configuration
public class ConsulConfiguration {
    @Value("${server.port}")
    private int port;

    @Value("${spring.application.name}")
    private String serviceId;

    @PostConstruct
    public void init() {
        // 参数完全对应HTTP API
        ImmutableRegistration.Builder builder = ImmutableRegistration.builder()
                .id(serviceId)
                .name("real_server")
                .address("127.0.0.1")
                .port(port)
                .addTags("dev");

        // 向Consul注册服务
        Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString("127.0.0.1:8500")).build();
        final AgentClient agentClient = consul.agentClient();
        agentClient.register(builder.build());

        //注册shutdown hook，停掉应用时从Consul摘除服务
        Runtime.getRuntime().addShutdownHook(new Thread(() -> agentClient.deregister(serviceId)));
    }
}
