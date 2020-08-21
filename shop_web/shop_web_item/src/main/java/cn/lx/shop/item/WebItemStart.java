package cn.lx.shop.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * cn.lx.shop.item
 *
 * @Author Administrator
 * @date 14:48
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.item","cn.lx.shop.exception"})
@EnableEurekaClient
@EnableFeignClients(basePackages = {"cn.lx.shop.goods.feign"})
public class WebItemStart {
    public static void main(String[] args) {
        SpringApplication.run(WebItemStart.class,args);
    }
}
