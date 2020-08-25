package cn.lx.shop;

import cn.lx.shop.oauth.util.JwtPayloadBuilder;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        LocalDateTime iat = LocalDateTime.now();
        //令牌中存放的信息
        Map<String,Object> payload=new HashMap<>(16);

        //payload.put("iat",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //payload.put("exp",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()+10000)));

        payload.put("scope",new String[]{"app"});
        payload.put("name",null);
        payload.put("id",null);
        payload.put("exp",1000000);
        payload.put("authorities",new String[]{"admin","oauth"});
        payload.put("jti", UUID.randomUUID().toString());
        payload.put("client_id", "shop");
        payload.put("username", "13904211939");
        String builder = JSON.toJSONString(payload);

        //创建令牌
        /*JwtPayloadBuilder payload = new JwtPayloadBuilder();
        payload.expDays(1);
        String builder = payload.builder();*/
        Jwt jwt = JwtHelper.encode(builder, new RsaSigner(privateKey));
        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
        //System.out.println(keyPair.getPublic().toString());

    }


    @Test
    public void  parseJwt(){
       //String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU5ODI3NjA0NywiYXV0aG9yaXRpZXMiOlsidGVzdCIsInVzZXIiXSwianRpIjoiOTJjNTIyM2MtZWI3Mi00OWU5LTk4MzAtNGJhM2M3ZWY5MzExIiwiY2xpZW50X2lkIjoic2hvcCIsInVzZXJuYW1lIjoiMTM5MDQyMTE5MzkifQ.ZALIaIBN5vHM_3quiGBbASOswKzbsx_ON9ys6qRPURUpTBheSnM0DE2KNKw8by0436fXVHau0aOyVzj2ZQWkCDmtiyusztvGNoB75QicQh8r1b0HrSDnMzzmgtMMBpxt6U0-Y2iymjZHlz_nhwcBtNr6dVOvFq_IbFSrJYOVJ47H_Td-aRE_ELMuokrdwzCETaDNe1bV-Cq5Xo2w6jT_MKWeizm7IwWnDaIF2E1o_JogFhsDkXApDPxgMp9gqwoqDFbpK9yAyYZiHhNfc1SML0Tu18mUsLGc0VnCs2v9BwZzUl3WY09x0u2_850vOo_UwnedMu0dQH_eLBgpvntVGw";
       //String token="";
       String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwiZXhwIjoxMDAwMDAwLCJhdXRob3JpdGllcyI6WyJhZG1pbiIsIm9hdXRoIl0sImp0aSI6IjQ4OWNkYThkLTliZTMtNDIyOS1iYzAxLWUxMGQyODI2YjJiNiIsImNsaWVudF9pZCI6InNob3AiLCJ1c2VybmFtZSI6IjEzOTA0MjExOTM5In0.h0xMsJM1XvpZDGT9szDl8Vg0YFebLx7kKdamaNTZzivO7j1TmVGzxrCXbeCpD5kSSj6ENqLi9NTM2fVftZYMmpZCSjVRd1yc1gr5eTCfB4nq21iUpu6vfiiwMkyYQyUa7oqvXuZz2bciF97Qz9LKHRMWNUVw-B7_0PcmBQ1xDHf0KpewmKmjApMkrRfxSqb31yYCYOwhpurpw9SnNdFbc8mOEXqfoLJ_UlGFmrTYj4fwQFUihnEY8I3w3xQKrBoMW8_DsVa9l9jCJwEL-hjdSCKzdERb8VPp0kh43PrnkwvRLk4_knmwd29Hjqny7JZDRO5WucTJz-z8l7Vp5Evhlw";
       String publicKey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw+grmbi3EzJNjuQ9kLUbiHvGSbDB7CvWV+5JU2U/PbI1sSxhgv61Yxz0mOFjie17yXwQV6SZD3565WIGFzoTeskIUBlYYHVM7EE1X9OiBa+wp5N396QCcQIXxAhmcfA2lUDJMQAVtuwHdXdwclEfMr8S+n+7z3Y3A1UgILSp9B5y9DMq55aC3alEDrF4vyrrjqR3An/tSr/f1c6JqVTqZ5O3ABLEveTFUKcYkHbjJ+pxrU2RAXD5nvdRzEFOJAZo1f9xOeXV6A830BqURYLuRxIqLq1XLa+Aaf3h+ianiSoSB1Bj+P0BI9xweOxqrCFTEMAuGlyHV3pOe3yGdqzO/wIDAQAB-----END PUBLIC KEY-----";
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        System.out.println(jwt.getClaims().toString());
    }


    @Test
    public void  parseJwtHeader(){
       //String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU5ODI3NjA0NywiYXV0aG9yaXRpZXMiOlsidGVzdCIsInVzZXIiXSwianRpIjoiOTJjNTIyM2MtZWI3Mi00OWU5LTk4MzAtNGJhM2M3ZWY5MzExIiwiY2xpZW50X2lkIjoic2hvcCIsInVzZXJuYW1lIjoiMTM5MDQyMTE5MzkifQ.ZALIaIBN5vHM_3quiGBbASOswKzbsx_ON9ys6qRPURUpTBheSnM0DE2KNKw8by0436fXVHau0aOyVzj2ZQWkCDmtiyusztvGNoB75QicQh8r1b0HrSDnMzzmgtMMBpxt6U0-Y2iymjZHlz_nhwcBtNr6dVOvFq_IbFSrJYOVJ47H_Td-aRE_ELMuokrdwzCETaDNe1bV-Cq5Xo2w6jT_MKWeizm7IwWnDaIF2E1o_JogFhsDkXApDPxgMp9gqwoqDFbpK9yAyYZiHhNfc1SML0Tu18mUsLGc0VnCs2v9BwZzUl3WY09x0u2_850vOo_UwnedMu0dQH_eLBgpvntVGw";
       //String token="";
       //String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6IiIsImlkIjoiIiwiZXhwIjoxMDAwMDAwLCJhdXRob3JpdGllcyI6WyJhZG1pbiIsIm9hdXRoIl0sImp0aSI6IjRjOGZjMzk3LWFkY2MtNDk0My1iMzFlLTk4MDdmYTNkY2ExZSIsImNsaWVudF9pZCI6InNob3AiLCJ1c2VybmFtZSI6IjEzOTA0MjExOTM5In0.piAJmXxQ6jvDOpNMOJS1Iq6aqz9PjWROZDWvJRUyWHuFhqFq3Nckwf8HV3_VRuHwu3s_fKfa5Q0cIR4QD-6qVel1_x6pP-jeGJUGLkp8mQpOmzFckI-DymqafNuQiBL3ZfisN2NUwjEiqEiqZqBI98KWzoam2mNRi0qx_u5sLUeB5G08RMIzK8t_YgT0P37eJjZ2a0N1cxVs5psetKJtRXDFLjtquyE65P4IOgJbqSN9504oXXoumwmks3wvMI3RQh1IdWkm7kKEOjIG2i93fonsaQlRgy1cl-WZIrgpY5pEeU22YwlfEBjlTZl13zACXtj7RG5aa34XfQNfhC4gLg";
       String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjEwMDAwMDAsImF1dGhvcml0aWVzIjpbImFkbWluIiwib2F1dGgiXSwianRpIjoiNjk4MTFhYzgtMDQ2Ni00Yzg2LWExYjctNDQyZWVhODk3NmYxIiwiY2xpZW50X2lkIjoic2hvcCIsInVzZXJuYW1lIjoiMTM5MDQyMTE5MzkifQ.V3Rp5MxI8tsC3mNtajZNjILB6E1rlnM8YNuNLEbh-B41yq2EhVpS-h2W4uNStTc2MVaYVxfWKQ3vI_QpwxJaZYxiyYYKIiLFm5hANDHveqPfdHj41mWnrIWjzgqsqo3AZcEgeg6GKr4x0ESFVWsEOgv0tnOhQ-YFMXb4bXKrpN-nRnoVoRCU04HNxp35Afj0TwSD9ZADtRkoovG1x72XWbIknGvmcbXJ_pVz0jdA9pEjonQdZNqZyBcqbcGkjh52WDXqc8um9C-IM9qx7YUZO0SQhTvZU049tIpPnfIYjO_tzEVwSQc7ZCgSULKXxU9A34L1L6wovBjO2CT1i6Kccw";
        Map<String, String> headers = JwtHelper.headers(token);
        System.out.println(headers.toString());
    }
}
