package springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建 TestController 类，提供测试 API 接口。
 * - 对于 /test/demo 接口，直接访问，无需登录。
 * - 对于 /test/home 接口，无法直接访问，需要进行登录。
 * - 对于 /test/admin 接口，需要登录「admin/admin」用户，因为需要 ADMIN 角色。
 * - 对于 /test/normal 接口，需要登录「normal/normal」用户，因为需要 NORMAL 角色。
 *
 * 可以按照如上的说明，进行各种测试。127.0.0.1:8080
 * 例如说，登录「normal/normal」用户后，去访问 /test/admin 接口，会返回 403 界面，无权限~
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/demo1")
    public String demo1() {
        return "免认证，示例返回1";
    }

    @GetMapping("/demo2")
    public String demo2() {
        return "免认证，示例返回2";
    }

    @GetMapping("/home")
    public String home() {
        return "我是首页";
    }

    @GetMapping("/admin")
    public String admin() {
        return "我是管理员";
    }

    @GetMapping("/normal")
    public String normal() {
        return "我是普通用户";
    }

    @GetMapping("/remix1")
    public String remix1() {
        return "同时两个权限";
    }

    @GetMapping("/remix2")
    public String remix2() {
        return "两个权限，拥有任意一个";
    }

    @PostMapping("/processing")
    public String processing() {
        return "填写验证登录的请求地址";
    }
    @PostMapping("/loginSuccess")
    public String loginSuccess() {
        return "登陆成功，自动跳转过来的页面";
    }


    @PostMapping("/loginFailure")
    public String loginFailure() {
        return "登陆失败！！自动跳转过来的页面";
    }

    @PostMapping("/logoutSuccess")
    public String logoutSuccess() {
        return "退出成功～～自动跳转过来的页面";
    }

}
