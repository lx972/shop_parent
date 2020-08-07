package cn.lx.shop.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * cn.lx.shop.goods
 * `MapperScan   tk.mybatis.spring.annotation`包下的，用于扫描Mapper接口
 *
 * @Author Administrator
 * @date 10:21
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"cn.lx.shop.goods.dao"})
public class GoodsStart {

    public static void main(String[] args) {
        SpringApplication.run(GoodsStart.class, args);
    }
}
