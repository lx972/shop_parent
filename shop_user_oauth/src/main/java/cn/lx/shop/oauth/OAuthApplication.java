package cn.lx.shop.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * cn.lx.shop
 * @Author Administrator
 * @date 15:54
 */
@SpringBootApplication(scanBasePackages = {"cn.lx.shop.oauth","cn.lx.shop.exception"})
@EnableDiscoveryClient
public class OAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication.class,args);
    }


    /**
     * springboot2.1取消了自动创建RestTemplate对象，必须手动构建
     * @param builder
     * @return
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }


}
