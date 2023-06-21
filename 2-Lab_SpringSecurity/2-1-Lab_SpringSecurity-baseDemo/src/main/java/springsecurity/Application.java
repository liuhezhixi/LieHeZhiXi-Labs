package springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * 1、项目启动成功后，浏览器访问 http://127.0.0.1:8080/admin/demo 接口。因为未登录，所以被 Spring Security 拦截到登录界面。
     * - 因为我们没有自定义登录界面，所以默认会使用 DefaultLoginPageGeneratingFilter 类，生成登陆界面。
     * - 只有输入正确的密码才能访问后面的资源
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
