import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
public class TestBCrypt {

    @Test
    public void testBCrypt() {

        //对密码进行加密
        String gensalt = BCrypt.gensalt();
        System.out.println("BCrypt自动生成的salt盐 = " + gensalt);//BCrypt自动生成的salt盐 = $2a$10$DWa.hJqYkfGUXFZjQggPhu
        //参数1=密码，参数2=自定义字符串密钥。这里使用的是BCrypt自动生成的salt盐（这个salt会放到最终加密后的密码中）
        String hashpw = BCrypt.hashpw("abiao", gensalt);
        System.out.println("密码进行加密 = " + hashpw);//密码进行加密 = $2a$10$DWa.hJqYkfGUXFZjQggPhursXjSE4mEc9Jy9l1XPCT/4qLpwfkmk2

        //校验密码
        boolean checkpw = BCrypt.checkpw("abiao", "$2a$10$DWa.hJqYkfGUXFZjQggPhursXjSE4mEc9Jy9l1XPCT/4qLpwfkmk2");
        System.out.println("密码进行校验 = " + checkpw);
    }
}
