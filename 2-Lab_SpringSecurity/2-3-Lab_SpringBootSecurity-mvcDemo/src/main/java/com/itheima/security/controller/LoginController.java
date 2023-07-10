package com.itheima.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
public class LoginController {

    @RequestMapping(value = "/login-success",produces = {"text/plain;charset=UTF-8"})
    public String loginSuccess(){
        //提示具体用户名称登录成功
        return getUsername()+" 登录成功";
    }

    /**
     * 测试资源1
     * @return
     */
    @GetMapping(value = "/r/r1",produces = {"text/plain;charset=UTF-8"})
    @PreAuthorize("hasAuthority('p1')")//拥有p1权限才可以访问
    public String r1(){
        return getUsername()+" 访问资源1";
    }

    /**
     * 测试资源2
     * @return
     */
    @GetMapping(value = "/r/r2",produces = {"text/plain;charset=UTF-8"})
    @PreAuthorize("hasAuthority('p2')")//拥有p2权限才可以访问
    public String r2(){
        return getUsername()+" 访问资源2";
    }


    /**
     * SpringSecurity 会话保存
     *
     * 用户认证通过后，为了避免用户的每次操作都进行认证可将用户的信息保存在会话中。
     * spring security提供会话管理，认证通过后将身份信息放入SecurityContextHolder上下文，SecurityContext与当前线程进行绑定，方便获取用户身份。
     */
    //获取当前用户信息
    private String getUsername(){
        String username = null;
        //得到， 当前认证通过的用户身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //用户身份
        Object principal = authentication.getPrincipal();
        if(principal == null){
            //如果没有登陆，就给他设置成默认值
            username = "匿名";
        }
        if(principal instanceof org.springframework.security.core.userdetails.UserDetails){
            UserDetails userDetails = (UserDetails) principal;
            username = userDetails.getUsername();
        }else{
            username = principal.toString();
        }
        return username;
    }
}
