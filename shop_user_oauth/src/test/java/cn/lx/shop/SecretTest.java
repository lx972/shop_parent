package cn.lx.shop;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * cn.lx.shop
 *
 * @Author Administrator
 * @date 17:19
 */
public class SecretTest {

    @Test
    public void  createJwt(){
        //读取shop.jks
        ClassPathResource resource = new ClassPathResource("shop.jks");
        //创建秘钥工厂 1.io流 2.证书的密码
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(resource,"shop123".toCharArray());
        //得到秘钥  1.证书别名 2.秘钥密码
        KeyPair keyPair = keyFactory.getKeyPair("shop", "shoptest".toCharArray());
        //强制转换为rsa算法加密的私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //令牌中存放的信息
        Map<String,Object> content=new HashMap<>(16);
        content.put("username","lx");
        content.put("sex","man");
        content.put("age","24");
        //创建令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(content), new RsaSigner(privateKey));
        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
        //System.out.println(keyPair.getPublic().toString());

    }


    @Test
    public void  parseJwt(){
       String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXgiOiJtYW4iLCJhZ2UiOiIyNCIsInVzZXJuYW1lIjoibHgifQ.IU8l0-IBrk6h3KflN8h2KTBjuiOYu-3H3kvqXrCQc-4XmZ044V0gcVN43GsWgMUKU88I1RaKXNi8eyQxQW-YAzTOvD9LUNMTU0YhXZ6H5dDqsyZtCazvxLJCwGa3LFzm6mDgloGu4qSDvORGzd7xmJcMwnPV1DaknJK5YfpU_2PxHl-Ea0BAAsK86gjwnyfm1LTzR7s0ZGNkZ21cPbbkA73mVv-gTqOazFyQzhmdi_8aMfJ7ApcRR7YalUrwfaNUnx36DygR6HaQiKYTgxVvn7Sd_pfb4oOW-kVvESG-1GOUFh_Dj-CdaEtxixCzx7MBxh6gHbAMT2mO0etti49l_Q";
       String publicKey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw+grmbi3EzJNjuQ9kLUbiHvGSbDB7CvWV+5JU2U/PbI1sSxhgv61Yxz0mOFjie17yXwQV6SZD3565WIGFzoTeskIUBlYYHVM7EE1X9OiBa+wp5N396QCcQIXxAhmcfA2lUDJMQAVtuwHdXdwclEfMr8S+n+7z3Y3A1UgILSp9B5y9DMq55aC3alEDrF4vyrrjqR3An/tSr/f1c6JqVTqZ5O3ABLEveTFUKcYkHbjJ+pxrU2RAXD5nvdRzEFOJAZo1f9xOeXV6A830BqURYLuRxIqLq1XLa+Aaf3h+ianiSoSB1Bj+P0BI9xweOxqrCFTEMAuGlyHV3pOe3yGdqzO/wIDAQAB-----END PUBLIC KEY-----";
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        System.out.println(jwt.getClaims().toString());
    }
}
