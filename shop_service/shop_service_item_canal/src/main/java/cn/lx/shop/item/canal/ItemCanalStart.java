package cn.lx.shop.item.canal;

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
 * @EnableFeignClients(basePackages = "cn.lx.shop.item.feign") 指向的api工程的item
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.exception","cn.lx.shop.item.canal"},exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableCanalClient
@EnableFeignClients(basePackages = {"cn.lx.shop.item.feign"})
public class ItemCanalStart {
    public static void main(String[] args) {
        SpringApplication.run(ItemCanalStart.class, args);
    }
}
