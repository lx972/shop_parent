package cn.lx.shop.entity;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成oauth2.0的令牌
 *
 * @Author Administrator
 * @date 15:54
 */
public class OauthJwtUtil {


    /**
     * 创建一个具有管理员权限的令牌
     * @return
     */
    public static String createJwt(){
        //读取证书
        ClassPathResource resource = new ClassPathResource("shop.jks");
        //构建秘钥工厂，输入密码读取证书
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, "shop123".toCharArray());
        //根据别名和秘钥库密码获取证书中的秘钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("shop", "shoptest".toCharArray());
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String,Object> content=new HashMap<>(16);
        //该令牌有管理员权限
        content.put("authorities",new String[]{"admin","oauth"});
        //content.put("authorities","admin");
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(content), new RsaSigner(privateKey));
        System.out.println(jwt.getEncoded());
        return "bearer "+jwt.getEncoded();
    }

}


