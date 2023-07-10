package com.itheima.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
       return NoOpPasswordEncoder.getInstance();
    }

    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * spring security为防止CSRF（Cross-site request forgery跨站请求伪造）的发生，限制了除了get以外的大多数方法。容易产生403。
         * 解决方法1：
         *  - 只需要：.csrf().disable()
         *  - 屏蔽CSRF控制，即spring security不再限制CSRF。
         * 解决方法2：
         *  - 在login.jsp页面添加一个token，spring security会验证token，如果token合法则可以继续请求。 修改login.jsp
         *  - <form action="login" method="post">
         *      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
         *      ...
         *    </form>
         */
        http.csrf().disable()
            .authorizeRequests()
            //.antMatchers("/r/r1").hasAuthority("p2")
            //.antMatchers("/r/r2").hasAuthority("p2"
            .antMatchers("/r/**").authenticated()//所有/r/**的请求必须认证通
            .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
            .and()
            .formLogin()//允许表单登录
                //.loginPage("/login-view")// 登录页面
                .loginProcessingUrl("/login")// 指定登录处理的URL，也就是用户名、密码表单提交的目的路径
                .successForwardUrl("/login-success")//自定义登录成功的页面地址
            .and()
                /**
                 * 会话控制：
                 * - always：如果没有session存在就创建一个
                 * - ifRequired（默认）：如果需要就创建一个Session登录时
                 * - never：SpringSecurity 将不会创建Session，但是如果应用中其他地方创建了Session，那么Spring Security将会使用它。
                 * - stateless：SpringSecurity将绝对不会创建Session，也不使用Session
                 *
                 * 默认情况下，Spring Security会为每个登录成功的用户会新建一个Session，就是ifRequired 。
                 * 若选用never，则指示Spring Security对登录成功的用户不创建Session了，但若你的应用程序在某地方新建了 session，那么Spring Security会用它的。
                 * 若使用stateless，则说明Spring Security对登录成功的用户不会创建Session了，你的应用程序也不会允许新建 session。并且它会暗示不使用cookie，所以每个请求都需要重新进行身份验证。这种无状态架构适用于REST API 及其无状态认证机制。
                 */
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login-view?logout");
        /**
         * 退出操作 拓展：
         * 1、定制的 LogoutSuccessHandler ，用于实现用户退出成功时的处理。如果指定了这个选项那么logoutSuccessUrl()的设置会被忽略。
         * 2、添加一个LogoutHandler ，用于实现用户退出时的清理工作（不管退出成功or退出失败）.默认SecurityContextLogoutHandler会被添加为最后一个 LogoutHandler
         * 3、指定是否在退出时让 Httpsession 无效。默认设置为 true。
         *
         * 注意：如果让logout在GET请求下生效，必须关闭防止CSRF攻击csrf().disable()。如果开启了CSRF，必须使用 post方式请求/logout
         *
         * logoutHandler：
         *  - 一般来说， LogoutHandler 的实现类被用来执行必要的清理，因而他们不应该抛出异常。
         *  - 下面是Spring Security提供的一些实现：
         *      - PersistentTokenBasedRememberMeServices 基于持久化token的RememberMe功能的相关清理
         *      - TokenBasedRememberMeService 基于token的RememberMe功能的相关清理
         *      - CookieClearingLogoutHandler 退出时Cookie的相关清理
         *      - CsrfLogoutHandler 负责在退出时移除csrfToken
         *      - SecurityContextLogoutHandler 退出时SecurityContext的相关清理
         *  - 链式API提供了调用相应的 LogoutHandler 实现的快捷方式，比如deleteCookies()。
         */
                /*.logoutSuccessHandler(logoutSuccessHandler)
                .addLogoutHandler(logoutHandler)
                .invalidateHttpSession(true);;*/


    }
}
