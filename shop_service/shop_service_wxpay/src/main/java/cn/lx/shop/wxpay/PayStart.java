package cn.lx.shop.wxpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * cn.lx.shop.wxpay
 *
 * @Author Administrator
 * @date 10:27
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.exception", "cn.lx.shop.wxpay"},exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
public class PayStart {

    public static void main(String[] args) {
        SpringApplication.run(PayStart.class, args);
    }

}
