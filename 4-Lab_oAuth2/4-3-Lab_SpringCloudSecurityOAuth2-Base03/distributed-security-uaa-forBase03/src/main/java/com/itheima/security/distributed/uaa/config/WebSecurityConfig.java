package com.itheima.security.distributed.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 针对url 安全访问策略
 **/
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //oAuth需要的 认证管理器
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    //密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/r/r1").hasAnyAuthority("p1")
                .antMatchers("/login*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 使用内存中的 InMemoryUserDetailsManager 的代码示例
         */
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
            .and().withUser("biao").password("biao").roles("NORMAL","ADMIN").authorities("p1"); //这里，不仅给biao用户设置了 "NORMAL" "ADMIN" 两种角色。还设置了 "p1" 权限
    }
}
