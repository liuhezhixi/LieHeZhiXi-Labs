package SpringSessionWithSpringSecurityAndRedisBaseDemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * 创建 SecurityConfig 配置类，继承 WebSecurityConfigurerAdapter 抽象类，实现 Spring Security 在 Web 场景下的自定义配置。
 * 我们可以通过重写 WebSecurityConfigurerAdapter 的方法，实现自定义的 Spring Security 的配置。
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                // <x> 使用内存中的 InMemoryUserDetailsManager
                        inMemoryAuthentication()
                // <y> 不使用 PasswordEncoder 密码编码器
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                // <z> 配置 admin 用户
                .withUser("admin").password("admin").roles("ADMIN")
                // <z> 配置 normal 用户
                .and().withUser("normal").password("normal").roles("NORMAL")
                // <z> 配置 biao 用户
                .and().withUser("biao").password("biao").roles("NORMAL","ADMIN");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .permitAll()
                .and()
                .logout()
                    .permitAll();
    }


}
