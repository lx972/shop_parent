package cn.lx.shop.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * cn.lx.shop.gateway.filter
 *
 * @Author Administrator
 * @date 10:38
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    //令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";



    /**
     * 对所有访问网关的请求进行过滤
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取登录页的url
        String url = getUserOauthUrl();

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //为true说明请求头中已包含令牌
        boolean flag=true;
        //放行不需要认证的请求
        String path = request.getPath().toString();
        if (URLFilter.hasAuthorize(path)){
            //登录请求，放行
            return chain.filter(exchange);
        }
        //请求头中是否包含令牌
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        if (StringUtils.isEmpty(token)){
            //请求头中没有令牌，就去参数中查找
            flag=false;
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        if (StringUtils.isEmpty(token)){
            //参数中没有令牌，就去cookie中查找
            HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (httpCookie!=null){
                token = httpCookie.getValue();
            }
        }
        if (StringUtils.isEmpty(token)){
            //该请求中没有令牌
            //response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //重定向到登录页
            redirectLogin(url,request.getURI().toString(), response);
            return response.setComplete();
        }

        if (!flag){
            //请求头中没有令牌
            token="bearer "+token;
            //在请求头中添加令牌
            request.mutate().header(AUTHORIZE_TOKEN,token);
        }else {
            //请求头中有令牌
            if (!token.startsWith("bearer ")&&!token.startsWith("Bearer ")){
                //令牌格式不对
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }
        //放行
        return chain.filter(exchange);
    }

    /**
     * 重定向到登录页
     * @param url   登录页
     * @param path  要访问的地址
     * @param response
     */
    private void redirectLogin(String url,String path, ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",url+"?from="+path);
    }

    /**
     * 获取登录页的url
     * @return
     */
    private String getUserOauthUrl() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-oauth");
        if (serviceInstance==null){
            throw new RuntimeException("user-oauth服务找不到");
        }
        String url = serviceInstance.getUri().toString()+"/oauth/login";
        System.out.println(url);
        return url;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
