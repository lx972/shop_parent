package cn.lx.shop.search;

import cn.lx.shop.interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * cn.lx.shop
 * SpringBootApplication 默认扫描和启动类同级的目录注解，而你的全局异常在别的工程，所以路径必须手动加入
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.search"})
@EnableEurekaClient
@EnableFeignClients(basePackages = "cn.lx.shop.search.feign")
public class WebSearchStart {

    public static void main(String[] args) {
        SpringApplication.run(WebSearchStart.class, args);
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
