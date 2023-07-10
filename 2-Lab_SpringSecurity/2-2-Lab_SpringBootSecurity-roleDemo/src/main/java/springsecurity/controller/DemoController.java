package springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/demo")
public class DemoController {

    /**
     * @PermitAll 注解，等价于 #permitAll() 方法，所有用户可访问。
     * - 重要！！！
     *      - 因为在 SecurityConfig 中，配置了 .anyRequest().authenticated() ，任何请求，访问的用户都需要经过认证。所以这里 @PermitAll 注解实际是不生效的。
     *      - /test/demo 能生效的原因是，在配置类中，antMatchers("/test/demo").permitAll() 比 .anyRequest().authenticated() 先配置。但是注解是在配置加载后才生效的，所以这里的@PermitAll 注解实际是不生效的。
     *      - 也就是说，Java Config 配置的权限，和注解配置的权限，两者是叠加的。
     */
    @PermitAll
    @GetMapping("/echo")
    public String demo() {
        return "示例返回";
    }

    @GetMapping("/home")
    public String home() {
        return "我是首页";
    }

    /**
     * @PreAuthorize 注解，等价于 #access(String attribute) 方法，，当 Spring EL 表达式的执行结果为 true 时，可以访问。
     *
     * Spring Security 还有其它注解，不过不太常用，可见《区别： @Secured(), @PreAuthorize() 及 @RolesAllowed()》文章。
     *  - https://www.iocoder.cn/Fight/Differences-secure-preauthorize-and-rolesallowed/?self
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "我是管理员";
    }

    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @GetMapping("/normal")
    public String normal() {
        return "我是普通用户";
    }


    /**
     * 法需要同时拥有p_transfer和p_read_account权限才能访问，底层使用WebExpressionVoter投票器，可从AﬃrmativeBased第23行代码跟踪。
     * @return
     */
    @PreAuthorize("hasAuthority('p_transfer') and hasAuthority('p_read_account')")
    @GetMapping("/authMix")
    public String authMix() {
        return "我是需要混合权限";
    }


    @PreAuthorize("isAnonymous()")
    public String findAccounts(){
        return "我是需要混合权限";
    };

}
