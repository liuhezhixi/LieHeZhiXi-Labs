package com.itheima.security.distributed.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootApplication
public class UAAServer {
    public static void main(String[] args) {
        SpringApplication.run(UAAServer.class, args);
    }
    /**
     * 授权码模式（演示）
     *
     * 1.去获得授权码
     *
     * 无痕浏览器请求：/uaa/oauth/authorize?client_id=c1&response_type=code&scope=all&redirect_uri=http://www.baidu.com
     * - 参数列表如下：
     *      - client_id ：客户端准入标识。
     *      - response_type ：授权码模式固定为code。
     *      - scope：客户端权限。
     *      - redirect_uri ：跳转uri，当授权码申请成功后会跳转到此地址，并在后边带上code参数（授权码）。
     * 在此项目中是：http://127.0.0.1:53020/uaa/oauth/authorize?client_id=c1&response_type=code&scope=all&redirect_uri=http://www.baidu.com
     *
     * - 跳出需要登陆的界面
     *      - 输入 SpringSecurity 的 UserDetails的密码，例如admin和admin。（通常来是请求数据库。这里暂时储存在内存中方便展示）
     * - 跳出OAuth Approval页面
     *      - 点击 Approve 授权
     * - 跳转到我们之前指定的redirect_uri页面（也就是http://www.baidu.com）
     *      - 此时url栏 = https://www.baidu.com/?code=sV6R31
     *      - code后面的值就是 oAuth的授权码
     *
     * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *
     * 2. 去获得oauth的令牌
     * 客户端拿着授权码向授权服务器索要访问access_token
     *
     * 无痕浏览器请求：/uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=authorization_code&code=sV6R31&redirect_uri=http://www.baidu.com
     * - 参数列表如下
     *      - client_id ：客户端准入标识。
     *      - client_secret ：客户端秘钥。
     *      - grant_type ：授权类型，填写authorization_code，表示授权码模式
     *      - code：授权码，就是刚刚获取的授权码，注意：授权码只使用一次就无效了，需要重新申请。
     *      - redirect_uri：申请授权码时的跳转url，一定和申请授权码时用的redirect_uri一致。
     * 在此项目中是：http://127.0.0.1:53020/uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=authorization_code&code=sV6R31&redirect_uri=http://www.baidu.com
     *
     * Ps：
     * 授权码模式，这种模式是四种模式（授权码模式、简化模式、密码模式、客户端模式）中最安全的一种模式。
     * 一般用于client是Web服务器端应用或第三方的原生App调用资源服务的时候。
     * 因为在这种模式中access_token不会经过浏览器或移动端的App，而是直接从服务端去交换，这样就最大限度的减小了令牌泄漏的风险。
     *
     * - 获得oauth令牌：
     * {
     *     "access_token": "b23ad1c1-f1ab-4f46-86e8-6a9e489c0460",
     *     "token_type": "bearer",
     *     "refresh_token": "ec3474a0-a23c-4da7-926a-674227fbb76e",
     *     "expires_in": 7199,
     *     "scope": "all"
     * }
     */
}
