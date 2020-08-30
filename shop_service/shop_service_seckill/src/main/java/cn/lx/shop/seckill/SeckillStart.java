package cn.lx.shop.seckill;

import cn.lx.shop.entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * cn.lx.shop.seckill
 *
 * @Author Administrator
 * @date 10:27
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.exception","cn.lx.shop.seckill"})
@EnableEurekaClient
@MapperScan(basePackages = "cn.lx.shop.seckill.dao")
@EnableScheduling
@EnableAsync
public class SeckillStart {

    public static void main(String[] args) {
        SpringApplication.run(SeckillStart.class, args);
    }


    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
