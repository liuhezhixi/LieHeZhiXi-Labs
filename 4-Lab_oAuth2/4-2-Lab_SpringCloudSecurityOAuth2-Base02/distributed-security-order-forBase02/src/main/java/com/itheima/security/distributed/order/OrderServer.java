package com.itheima.security.distributed.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServer {
    public static void main(String[] args) {
        SpringApplication.run(OrderServer.class, args);
    }

    /**
     * 请求顺序
     * 1、首先使用授权服务uaa密码授权模式，用biao用户获得uaa返回的token
     * 因为biao用户拥有"p1"权限
     *
     * ------------------------------------------------------------------------------------------
     *
     * 2、测试资源服务
     * 2-1：
     *  - 直接调用资源服务的资源接口：127.0.0.1:53021/order/r1
     *      - 返回：{
     *              "error": "unauthorized",
     *              "error_description": "Full authentication is required to access this resource"
     *             }
     *      - 因为该请求没有携带授权服务颁发的token，所以无法通过验证。
     *
     * 2-2
     *  - 首先调用uaa的客户端模式获得token
     *  - 然后在调用资源服务的资源接口：127.0.0.1:53021/order/r1。的header中添加 [参数名=Authorization，value=Bearer xxxxxxx] 的token值。
     *  - 返回：{
     *          "error": "access_denied",
     *          "error_description": "不允许访问"
     *         }
     *  - 因为在uaa请求的客户端模式的token，没有对应的用户信息，自然没有对应的是否授权"p1"的信息。
     *
     * 2-3
     *  - 首先调用uaa的密码模式获得token（使用用户名为biao，因为这个用户名在uaa中配置了"p1"的资源权限）
     *  - 然后在调用资源服务的资源接口：127.0.0.1:53021/order/r1。的header中添加 [参数名=Authorization，value=Bearer xxxxxxx] 的token值。
     *  - 返回：访问资源1，成功
     *  - 因为在授权服务中对biao用户配置了"p1"资源权限，所以在资源服务的该资源请求上，可以通过SpringSecurity对该接口的权限校验。
     */
}
