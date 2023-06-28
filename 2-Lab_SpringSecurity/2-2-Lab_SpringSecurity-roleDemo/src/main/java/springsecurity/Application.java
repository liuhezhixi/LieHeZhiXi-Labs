package springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * 这个demo中，我们将会自定义 Spring Security 的配置，实现权限控制。
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
