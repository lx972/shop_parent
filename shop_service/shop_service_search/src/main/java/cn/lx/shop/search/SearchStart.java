package cn.lx.shop.search;

import cn.lx.shop.interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * cn.lx.shop
 *SpringBootApplication 默认扫描和启动类同级的目录注解，而你的全局异常在别的工程，所以路径必须手动加入
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.exception","cn.lx.shop.search"},exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients(basePackages = "cn.lx.shop.goods.feign")
public class SearchStart {

    public static void main(String[] args) {

        /**
         * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * availableProcessors is already set to [12], rejecting [12]
         * Elasticsearch 和 Redis 底层都使用到了 Netty , 在项目启动时会冲突
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(SearchStart.class, args);
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
