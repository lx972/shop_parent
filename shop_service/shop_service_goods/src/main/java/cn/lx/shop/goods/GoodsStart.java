package cn.lx.shop.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * cn.lx.shop
 *
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.lx.shop.goods.dao")
public class GoodsStart {
    public static void main(String[] args) {
        SpringApplication.run(GoodsStart.class,args);
    }
}
