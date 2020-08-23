package cn.lx.shop.oauth.service;

import cn.lx.shop.oauth.util.AuthToken;

/**
 * cn.lx.shop.oauth.service
 *
 * @Author Administrator
 * @date 18:04
 */
public interface AuthService {

    /**
     * 授权认证方法
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @param grant_type
     * @return
     */
    AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) throws Exception;
}
