package cn.lx.shop.oauth.service.impl;

import cn.lx.shop.oauth.service.AuthService;
import cn.lx.shop.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * cn.lx.shop.oauth.service.impl
 *
 * @Author Administrator
 * @date 18:05
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 密码授权认证方法
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @param grant_type
     * @return
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) throws Exception{
        //动态获取某微服务的uri
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-oauth");
        if (null==serviceInstance){
            throw new RuntimeException("找不到user-oauth微服务");
        }
        String uri = serviceInstance.getUri().toString();
        //模拟使用postman中的密码授权的请求http://localhost:9001/oauth/token
        String url=uri+"/oauth/token";
        //封装请求头
        MultiValueMap<String, String> headers=new LinkedMultiValueMap<String, String>();
        //模拟Basic c2hvcDpzaG9w
        //c2hvcDpzaG9w  是base64加密的clientId:clientSecret
        String Authorization="Basic "+ Base64.getEncoder().encodeToString((clientId+":"+clientSecret).getBytes());
        headers.add("Authorization",Authorization);
        //封装请求体
        MultiValueMap<String, String> body=new LinkedMultiValueMap<String, String>();
        body.add("grant_type",grant_type);
        body.add("username",username);
        body.add("password",password);
        //封装请求头和请求体
        HttpEntity<Object> httpEntity = new HttpEntity<>(body,headers);
        //发送请求，返回响应
        ResponseEntity<Map> mapResponseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map resultMap = mapResponseEntity.getBody();
        if (resultMap==null||resultMap.size()<1){
            throw new RuntimeException("创建令牌失败");
        }
        //处理响应
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(resultMap.get("access_token").toString());
        authToken.setRefreshToken(resultMap.get("refresh_token").toString());
        authToken.setJti(resultMap.get("jti").toString());
        return authToken;
    }
}
