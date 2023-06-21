package springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    /**
     * 提供一个 "/admin/demo" 接口，用于测试未登录时，会被拦截到界面。
     * @return
     */
    @GetMapping("/demo")
    public String demo() {
        return "示例返回";
    }

}
