package cn.lx.shop.item;

import cn.lx.shop.interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

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

    /**
     * 注入feign请求的拦截器
     * @return
     */
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
}
