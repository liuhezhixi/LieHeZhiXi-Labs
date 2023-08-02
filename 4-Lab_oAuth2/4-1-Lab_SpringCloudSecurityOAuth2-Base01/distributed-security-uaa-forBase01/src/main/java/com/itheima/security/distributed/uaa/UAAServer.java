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

    /**
     * 简化模式（演示）
     *
     * 1.资源拥有者打开客户端，客户端要求资源拥有者给予授权，它将浏览器被重定向到授权服务器，重定向时会附加客户端的身份信息。
     * - 无痕浏览器请求：/uaa/oauth/authorize?client_id=c1&response_type=token&scope=all&redirect_uri=http://www.baidu.com
     *      - 参数描述同授权码模式 ，注意response_type=token，说明是简化模式。
     * - 参数列表如下：
     *      - client_id ：客户端准入标识。
     *      - response_type ：简化模式固定为token。
     *      - scope：客户端权限。
     *      - redirect_uri ：跳转uri，当授权码申请成功后会跳转到此地址，并在后边带上code参数（授权码）。
     * 在此项目中是：http://127.0.0.1:53020/uaa/oauth/authorize?client_id=c1&response_type=token&scope=all&redirect_uri=http://www.baidu.com
     *
     *
     *
     * - 跳出需要登陆的界面
     *      - 输入 SpringSecurity 的 UserDetails的密码，例如admin和admin。（通常来是请求数据库。这里暂时储存在内存中方便展示）
     * - 跳出OAuth Approval页面
     *      - 点击 Approve 授权
     * - 跳转到我们之前指定的redirect_uri页面（也就是http://www.baidu.com）
     *      - 此时url栏 = https://www.baidu.com/#access_token=38bbbdbb-bbf4-4dec-b3b6-fc89e242addf&token_type=bearer&expires_in=7199
     *      - access_token后面的值就是 oauth的令牌
     *      - 授权服务器将授权码将令牌（access_token）以Hash的形式存放在重定向uri的fragment中发送给浏览器。
     *          - 注：fragment 主要是用来标识 URI 所标识资源里的某个资源，在 URI 的末尾通过（#）作为 fragment 的开头， 其中 # 不属于 fragment 的值。
     *          - 如https://domain/index#L18这个 URI 中 L18 就是 fragment 的值。
     *          - 大家只需要 知道js通过响应浏览器地址栏变化的方式能获取到fragment 就行了。
     *
     * - 一般来说，简化模式用于没有服务器端的第三方单页面应用，因为没有服务器端就无法接收授权码。
     */

    /**
     * 密码模式（演示）
     *
     * 1.资源拥有者将用户名、密码发送给客户端
     *
     * 2.客户端拿着资源拥有者的用户名、密码向授权服务器请求令牌（access_token），请求如下：
     *  - /uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=biao&password=biao
     *  - 参数列表如下：
     *     - client_id ：客户端准入标识。
     *     - client_secret ：客户端秘钥。
     *     - grant_type：授权类型，填写password表示密码模式
     *     - username：资源拥有者用户名。
     *     - password：资源拥有者密码。
     *  - 在此项目中是：
     *      - 在apiFox中使用post请求：
     *      - http://127.0.0.1:53020/uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=biao&password=biao
     *   - 请求返回结果：
     *   - {
     *       "access_token": "e46bc30d-4972-431b-9c56-f55d8a943a53",
     *       "token_type": "bearer",
     *       "refresh_token": "3b6b5a43-0f67-4060-84c1-308134cf5554",
     *       "expires_in": 7199,
     *       "scope": "all"
     *     }
     *
     * 3.授权服务器将令牌（access_token）发送给client这种模式十分简单，但是却意味着直接将用户敏感信息泄漏给了client
     *  - 因此这就说明这种模式只能用于client是我们自己开发的情况下。
     *  - 因此密码模式一般用于我们自己开发的，第一方原生App或第一方单页面应用。
     *
     *
     */

    /**
     * 客户端模式（演示）
     *
     * 1.客户端向授权服务器发送自己的身份信息，并请求令牌（access_token）
     * 2.确认客户端身份无误后，将令牌（access_token）发送给client，请求如下：
     *  - /uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=client_credentials
     *  - 参数列表如下：
     *      - client_id ：客户端准入标识。
     *      - client_secret ：客户端秘钥。
     *      - grant_type ：授权类型，填写client_credentials表示客户端模式 这种模式是最方便但最不安全的模式。因此这就要求我们对client完全的信任，而client本身也是安全的。因此这种模式一般用来提供给我们完全信任的服务器端服务。比如，合作方系统对接，拉取一组用户信息。
     *  - 在此项目中是：
     *      - 在apiFox中使用post请求：
     *      - http://127.0.0.1:53020/uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=client_credentials
     *      - 返回参数：
     *      - {
     *            "access_token": "76d80dc1-d184-412f-884a-2e44cbed8f74",
     *            "token_type": "bearer",
     *            "expires_in": 7199,
     *            "scope": "all"
     *         }
     */
}
