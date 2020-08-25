package cn.lx.shop.gateway.filter;

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

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //为true说明请求头中已包含令牌
        boolean flag=true;
        //放行不需要认证的请求
        String path = request.getPath().toString();
        if (!URLFilter.hasAuthorize(path)){
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
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
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

    @Override
    public int getOrder() {
        return 0;
    }
}
