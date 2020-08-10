package cn.lx.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * cn.lx.shop
 * exclude = {DataSourceAutoConfiguration.class}    排除掉数据库自动连接，应为加入了数据库依赖
 *
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class FileStart {
    public static void main(String[] args) {
        SpringApplication.run(FileStart.class,args);
    }
}
