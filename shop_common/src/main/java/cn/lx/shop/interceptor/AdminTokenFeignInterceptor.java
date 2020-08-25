package cn.lx.shop.interceptor;

import cn.lx.shop.entity.OauthJwtUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 在feign请求中加入一个管理员令牌
 *
 * @Author Administrator
 * @date 16:08
 */
public class AdminTokenFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", OauthJwtUtil.createJwt());
    }
}
