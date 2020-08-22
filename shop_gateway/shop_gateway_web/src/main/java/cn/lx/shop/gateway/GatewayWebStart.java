package cn.lx.shop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * cn.lx.shop
 *
 * @Author Administrator
 * @date 9:32
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayWebStart {

    public static void main(String[] args) {
        SpringApplication.run(GatewayWebStart.class,args);
    }

    /**
     * 得到远程客户的ip
     * @return
     */
    @Bean(name = "userKeyResolver")
    public KeyResolver userKeyResolver(){
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //得到远程客户的ip
                String ip = exchange.getRequest().getRemoteAddress().getHostString();
                System.out.println("ip:"+ip);
                return Mono.just(ip);
            }
        };
    }
}
