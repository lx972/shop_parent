import cn.lx.shop.entity.BCrypt;
import org.junit.Test;

/**
 * PACKAGE_NAME
 *
 * @Author Administrator
 * @date 9:44
 */
public class BCryptTest {


    @Test
    public void test1(){
        String hashpw = BCrypt.hashpw("123", BCrypt.gensalt(12));
        System.out.println(hashpw);
    }

    @Test
    public void test2(){
        boolean flag = BCrypt.checkpw("123", "$2a$12$/ZtJetisOeaZBTMFDujeZ.Xc7YEgdwv5Bf3gj7N/T.acIFVmtJsBu");
        System.out.println(flag);
    }
}
