package com.itheima.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SecuritySpringBootApp {
    /**
     * TODO LGS 2023/7/3:
     *  1、目前这个springBoot跑不起来，登陆login的时候404错误，无法找到login.jsp
     *  2、直接使用Spring Security自带的login也有问题
     */
    public static void main(String[] args) {
        SpringApplication.run(SecuritySpringBootApp.class,args);
    }
}
