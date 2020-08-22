import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * PACKAGE_NAME
 *
 * @Author Administrator
 * @date 10:07
 */
public class JwtTest {

    @Test
    public void Test1(){
        JwtBuilder jwtBuilder = Jwts.builder();
        //设置令牌签发者
        jwtBuilder.setIssuer("lx");
        //设置唯一编号
        jwtBuilder.setId("999");
        //设置令牌主题
        jwtBuilder.setSubject("XXXXX");
        //设置令签发日期
        jwtBuilder.setIssuedAt(new Date());
        //设置令过期时间
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis()+100000));
        //设置签名
        jwtBuilder.signWith(SignatureAlgorithm.HS256,"ittest");

        //自定义cliam
        Map<String,Object> map=new HashMap<>(16);
        map.put("name","234567");
        map.put("sex","1");
        map.put("pwd","2");
        jwtBuilder.addClaims(map);
        //得到令牌
        System.out.println(jwtBuilder.compact());
    }


    @Test
    public void Test2(){
        String jwt="eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJseCIsImp0aSI6Ijk5OSIsInN1YiI6IlhYWFhYIiwiaWF0IjoxNTk4MDYzMTI4LCJleHAiOjE1OTgwNjMyMjgsInNleCI6IjEiLCJuYW1lIjoiMjM0NTY3IiwicHdkIjoiMiJ9.pGbKvWT_xtizBSUh9ne4k-LS3gkz0AuI_R8iccFS4OM";
        Claims claims = Jwts.parser()
                .setSigningKey("ittest")
                .parseClaimsJws(jwt)
                .getBody();
        System.out.println(claims);
    }
}
