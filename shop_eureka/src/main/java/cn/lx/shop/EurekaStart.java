package cn.lx.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * cn.lx.shop
 *
 * @Author Administrator
 * @date 9:32
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaStart {

    public static void main(String[] args) {
        SpringApplication.run(EurekaStart.class,args);
    }
}
