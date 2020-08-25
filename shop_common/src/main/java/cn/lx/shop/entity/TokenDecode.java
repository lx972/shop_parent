package cn.lx.shop.entity;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * cn.lx.shop.order.config
 * 解析令牌
 * @Author Administrator
 * @date 12:06
 */
public class TokenDecode {

    //公钥
    private static final String PUBLIC_KEY = "public.key";


    /**
     * 读取公钥文件中的公钥
     * @return
     */
    public static String readPublicKey(){
        ClassPathResource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStream is = resource.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.readLine();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取令牌
     * @return
     */
    public static String getToken(){
        //获取授权信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        return details.getTokenValue();
    }

    /**
     * 解析令牌并将令牌中的数据转化为map集合
     * @return
     */
    public static Map<String,String> decodeToken(){
        //解析并校验令牌
        Jwt jwt = JwtHelper.decodeAndVerify(getToken(), new RsaVerifier(readPublicKey()));
        //获取令牌中的数据
        String claims = jwt.getClaims();
        Map parseObject = JSON.parseObject(claims, Map.class);
        return parseObject;
    }
}
