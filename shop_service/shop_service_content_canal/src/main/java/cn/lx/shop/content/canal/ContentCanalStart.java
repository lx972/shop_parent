package cn.lx.shop.content.canal;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * cn.lx.shop
 *SpringBootApplication 默认扫描和启动类同级的目录注解，而你的全局异常在别的工程，所以路径必须手动加入
 * exclude={DataSourceAutoConfiguration.class}去掉数据库的自动加载
 * @EnableFeignClients(basePackages = "cn.lx.shop.content.feign") 指向的api工程的feign
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.exception","cn.lx.shop.content.canal"},exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableCanalClient
@EnableFeignClients(basePackages = {"cn.lx.shop.content.feign"})
public class ContentCanalStart {
    public static void main(String[] args) {
        SpringApplication.run(ContentCanalStart.class, args);
    }
}
