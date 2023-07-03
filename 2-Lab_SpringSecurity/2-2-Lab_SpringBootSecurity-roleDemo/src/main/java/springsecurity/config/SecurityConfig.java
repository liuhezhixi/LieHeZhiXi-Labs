package springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * 创建 SecurityConfig 配置类，继承 WebSecurityConfigurerAdapter 抽象类，实现 Spring Security 在 Web 场景下的自定义配置。
 * 我们可以通过重写 WebSecurityConfigurerAdapter 的方法，实现自定义的 Spring Security 的配置。
 */
@Configuration
//修改 SecurityConfig 配置类，增加 @EnableGlobalMethodSecurity 注解，开启对 Spring Security 注解的支持，进行权限验证。详情看DemoController接口上面注释
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /** 配置用户信息服务（查询用户信息）
     *
     * 首先，我们重写 #configure(AuthenticationManagerBuilder auth) 方法，实现 AuthenticationManager 认证管理器。注意这是认证！
     * - 这里的入参 AuthenticationManagerBuilder ，来自父类 WebSecurityConfigurerAdapter
     *
     *
     * <X> 处，调用 AuthenticationManagerBuilder#inMemoryAuthentication() 方法，使用内存级别的 InMemoryUserDetailsManager Bean 对象，提供认证的用户信息。
     * - Spring 内置了两种 UserDetailsManager 实现：
     *  - InMemoryUserDetailsManager，和「2. 快速入门」是一样的。
     *  - JdbcUserDetailsManager ，基于 JDBC的 JdbcUserDetailsManager 。
     * - 实际项目中，我们更多采用调用 AuthenticationManagerBuilder#userDetailsService(userDetailsService) 方法，使用自定义实现的 UserDetailsService 实现类，更加灵活且自由的实现认证的用户信息的读取。
     *
     *
     * <Y> 处，调用 AbstractDaoAuthenticationConfigurer#passwordEncoder(passwordEncoder) 方法，设置 PasswordEncoder 密码编码器。
     * - 密码编码器的作用，是把查询出来的用户密码和用户输入的密码进行比对的方式。
     * - 在这里，为了方便，我们使用 NoOpPasswordEncoder 。实际上，等于不使用 PasswordEncoder ，不配置的话会报错。
     * - 生产环境下，推荐使用 BCryptPasswordEncoder 。更多关于 PasswordEncoder 的内容，推荐阅读《该如何设计你的 PasswordEncoder?》文章。【https://www.iocoder.cn/Spring-Security/laoxu/PasswordEncoder/?self】
     *
     *
     * <Z> 处，配置了「admin/admin」和「normal/normal」两个用户，分别对应 ADMIN 和 NORMAL 角色。相比在application.yaml中来说，可以配置更多的用户。
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 使用内存中的 InMemoryUserDetailsManager 的代码示例
         */
/*        auth.
            // <x> 使用内存中的 InMemoryUserDetailsManager
            inMemoryAuthentication()
            // <y> 不使用 PasswordEncoder 密码编码器
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            // <z> 配置 admin 用户
            .withUser("admin").password("admin").roles("ADMIN")
            // <z> 配置 normal 用户
            .and().withUser("normal").password("normal").roles("NORMAL")
            // <z> 配置 biao 用户
            .and().withUser("biao").password("biao").roles("NORMAL","ADMIN");*/

        /**
         * 使用 UserDetailsManager 从数据库中抓取数据方式的代码示例
         *
         * 使用 BCryptPasswordEncoder 实现其他的编码解析器，这里用BCryptPasswordEncoder示范（可以看test包里面的TestBCrypt.java）。
         * 如果有其他需求，就模仿BCryptPasswordEncoder实现PasswordEncoder重写其他方法即可
         */
        auth
                .userDetailsService(new CustomUserDetails())
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    /** 配置安全拦截机制
     *
     * 然后，我们重写 #configure(HttpSecurity http) 方法，主要配置 URL 的权限控制。
     *
     *
     * <X> 处，调用 HttpSecurity#authorizeRequests() 方法，开始配置 URL 的权限控制。注意看艿艿配置的四个权限控制的配置。下面，是配置权限控制会使用到的方法：
     * - antMatchers(String... antPatterns) 方法，配置匹配的 URL 地址，基于 Ant 风格路径表达式 ，可传入多个。
     *      - /test/*   一个*，代表通配test的下一级
     *      - /test/**  两个*，代表递归通配test的下面所有的层级
     * - 【常用】#permitAll() 方法，所有用户可访问。
     * - 【常用】#denyAll() 方法，所有用户不可访问。
     * - 【常用】#authenticated() 方法，登录用户可访问。
     * - #anonymous() 方法，无需登录，即匿名用户可访问。
     * - #rememberMe() 方法，通过 remember me 登录的用户可访问。
     * - #fullyAuthenticated() 方法，非 remember me 登录的用户可访问。
     * - #hasIpAddress(String ipaddressExpression) 方法，来自指定 IP 表达式的用户可访问。
     * - 【常用】#hasRole(String role) 方法， 拥有指定角色的用户可访问。
     * - 【常用】#hasAnyRole(String... roles) 方法，拥有指定任一角色的用户可访问。
     * - 【常用】#hasAuthority(String authority) 方法，拥有指定权限(authority)的用户可访问。
     * - 【常用】#hasAuthority(String... authorities) 方法，拥有指定任一权限(authority)的用户可访问。
     * - 【最牛】#access(String attribute) 方法，当 Spring EL 表达式的执行结果为 true 时，可以访问。
     *
     *
     * <Y> 处，调用 HttpSecurity#formLogin() 方法，设置 Form 表单登录。
     * - 如果胖友想要自定义登录页面，可以通过 #loginPage(String loginPage) 方法，来进行设置。不过如果使用security默认的登录界面，这里可以不进行设置。
     *
     *
     * <Z> 处，调用 HttpSecurity#logout() 方法，配置退出相关。
     * - 如果胖友想要自定义退出页面，可以通过 #logoutUrl(String logoutUrl) 方法，来进行设置。不过如果使用security默认的退出界面，这里可以不进行设置。
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() //security4.0之后会默认开启csrf安全验证,需要我们手动去关闭.
            // <x> 配置请求地址的权限
            .authorizeRequests()
                .antMatchers("/test/demo1","/test/demo2").permitAll() // 所有用户可访问
                .antMatchers("/test/admin").hasRole("ADMIN") // 需要 ADMIN 角色
                .antMatchers("/test/normal").access("hasRole('ROLE_NORMAL')") // 需要 NORMAL 角色。
                .antMatchers("/test/remix1").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_NORMAL')") // 需要同时有用两个权限
                .antMatchers("/test/remix2").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_NORMAL')") //两个权限，拥有其中一个即可
                .antMatchers("/test/justNeedAuthenticated").authenticated() //访问次路径必须经过认证
                // 任何请求，访问的用户都需要经过认证
                //.anyRequest().authenticated()
                .anyRequest().permitAll()
            .and()
            // <y> 设置 Form 表单登陆
            .formLogin()
                //.loginPage("/login-view") // 自定义 登陆 URL 地址 todo 待研究，如何让mvc自定义待jsp登陆页面生效
                //.loginProcessingUrl("/loginProcessingUrl") //填写验证登录的请求地址 todo 待研究怎么用
                .successForwardUrl("/test/loginSuccess") //设置登陆成功，自动跳转页面。注意这里接口必须是post！
                .failureForwardUrl("/test/loginFailure") //设置登陆失败，自动跳转页面。注意这里接口必须是post！
                //.permitAll() // 所有用户可访问
            .and()
            // <z> 配置退出相关
            .logout()
                //.logoutUrl("/logout") // 自定义 退出 URL 地址
                .logoutSuccessUrl("/test/logoutSuccess"); //设置退出成功，自动跳转页面。注意这里接口必须是post！
                //.permitAll()// 所有用户可访问
            //todo 待研究
            // 添加权限不足跳转,以及cookie保存
 /*           .and()
            .exceptionHandling().accessDeniedPage("/security/denied")
            .and()
            .rememberMe()
            .key("jbzm-Security")
            .rememberMeCookieName("cookieName")
            .rememberMeParameter("paramName")
            .tokenRepository(new InMemoryTokenRepositoryImpl());*/
    }


}
