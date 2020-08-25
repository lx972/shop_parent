package cn.lx.shop.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * cn.lx.shop
 *SpringBootApplication 默认扫描和启动类同级的目录注解，而你的全局异常在别的工程，所以路径必须手动加入
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.exception","cn.lx.shop.user"})
@EnableEurekaClient
@MapperScan(basePackages = "cn.lx.shop.user.dao")
public class UserStart {

    public static void main(String[] args) {
        SpringApplication.run(UserStart.class, args);
    }
}
