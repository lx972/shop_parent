package cn.lx.shop.goods;

import cn.lx.shop.entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * cn.lx.shop
 *SpringBootApplication 默认扫描和启动类同级的目录注解，而你的全局异常在别的工程，所以路径必须手动加入
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.exception","cn.lx.shop.goods","cn.lx.shop.seata"})
@EnableEurekaClient
@MapperScan("cn.lx.shop.goods.dao")
public class GoodsStart {
    public static void main(String[] args) {
        SpringApplication.run(GoodsStart.class, args);
    }


    /***
     * IdWorker ID生成
     * @return
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(0, 0);
    }
}
