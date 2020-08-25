package cn.lx.shop;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * cn.lx.shop
 *
 * @Author Administrator
 * @date 16:32
 */
public class BCryptTest {
    @Test
    public void test1(){
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
    }
}
