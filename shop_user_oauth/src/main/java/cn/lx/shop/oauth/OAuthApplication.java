package cn.lx.shop.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * cn.lx.shop
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.oauth","cn.lx.shop.exception"})
@EnableDiscoveryClient
public class OAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication.class,args);
    }


}
