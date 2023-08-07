package com.itheima.security.distributed.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * @author Administrator
 * @version 1.0
 * 授权服务配置
 **/
@Configuration
/**
 * 可以用 @EnableAuthorizationServer 注解指定的配置类
 * 同时该指定配置类，继承AuthorizationServerConfigurerAdapter来配置OAuth2.0授权服务器的相关配置。
 *
 * 如此证明该项目是 授权服务项目（通常来说都叫做UAA），或者带有授权服务功能的项目
 *
 * - 其中有三大配置
 *      - ClientDetailsServiceConfigurer ：用来配置客户端详情服务（ClientDetailsService），客户端详情信息在 这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
 *      - AuthorizationServerEndpointsConfigurer：用来配置令牌（token）的访问端点和令牌服务(token services)。
 *      - AuthorizationServerSecurityConfigurer：用来配置令牌端点的安全策略.
 *  - 授权服务配置总结：授权服务配置分成三大块，可以关联记忆。
 *      - 既然要完成认证，它首先得知道客户端信息从哪儿读取，因此要进行客户端详情配置。
 *      - 既然要颁发token，那必须得定义token的相关endpoint，以及token如何存取，以及客户端支持哪些类型的 token。
 *      - 既然暴露除了一些endpoint，那对这些endpoint可以定义一些安全上的约束策略等。
 */
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;


    /**
     * 客户端管理服务
     *
     *
     * - ClientDetailsServiceConfigurer ：用来配置客户端详情服务（ClientDetailsService）
     *      - 客户端详情信息在 这里进行初始化
     *      - 你能够把客户端详情信息（AuthorizationServerSecurityConfigurer）写死在这里或者是通过数据库来存储调取详情信息。
     *
     * - ClientDetails有几个重要的属性如下列表：
     *      - 1、clientId ：（必须的）用来标识客户的Id。
     *      - 2、secret：（需要值得信任的客户端）客户端安全码，如果有的话。
     *      - 3、scope ：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
     *      - 4、authorizedGrantTypes ：此客户端可以使用的授权类型，默认为空。
     *      - 5、authorities：此客户端可以使用的权限（基于Spring Security authorities）。
     *
     * - 客户端详情（Client Details）能够在应用程序运行的时候进行更新，
     *      - 可以通过访问底层的存储服务（例如将客户 端详情存储在一个关系数据库的表中，就可以使用 JdbcClientDetailsService）
     *      - 或者通过自己实现 ClientRegistrationService接口（同时你也可以实现 ClientDetailsService 接口）来进行管理。
     *
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception  {

        /**
         * 我们暂时使用内存方式存储客户端详情信息，配置如下:
         */
        clients.inMemory()// 使用in-memory存储
                .withClient("c1")// client_id，客户端id
                .secret(new BCryptPasswordEncoder().encode("secret"))//app_secret，客户端密钥。此处使用了BCrypt对密码进行了加密
                .resourceIds("res1")//客户端可以访问的资源列表
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")// 该client允许的申请令牌的方式（authorization_code,password,refresh_token,implicit,client_credentials）
                .scopes("all")// 允许的授权范围（客户端的权限，这个字段其实是自定义值的，定义的值，来后期来设置该客户端是否有权限校验）
                .autoApprove(false)//false代表，如果我们使用授权码模式，授权的时候会跳转到授权页面
                .redirectUris("http://www.baidu.com")//加上验证回调地址
                .and()//配置多个客户端
                .withClient("c2")
                .secret(new BCryptPasswordEncoder().encode("secret2"))
                .resourceIds("res2")
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")
                .scopes("all2")
                .autoApprove(false)
                .redirectUris("http://www.baidu.com");
    }


    /**
     * 令牌访问端点
     *
     * AuthorizationServerEndpointsConfigurer：用来配置令牌（token）的访问端点和令牌服务(token services)。
     * - 配置令牌服务(token services)：申请令牌的URL地址
     * - 配置令牌服务(token services)：令牌如何发放，令牌如何生成
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        /**
         * 配置授权类型（Grant Types）
         *
         * AuthorizationServerEndpointsConﬁgurer 通过设定以下属性决定支持的授权类型（Grant Types）:
         *      - authenticationManager：认证管理器，当你选择了资源所有者密码（password）授权类型的时候，请设置这个属性注入一个 AuthenticationManager 对象。
         *      - userDetailsService ：如果你设置了这个属性的话，那说明你有一个自己的 UserDetailsService 接口的实现（参考2-2-Lab的 UserDetails，主要实现从数据库中查询对应user信息的接口），
         *                             或者你可以把这个东西设置到全局域上面去（例如 GlobalAuthenticationManagerConfigure 这个配置对象），
         *                             当你设置了这个之后，那么 "refresh_token" 即刷新令牌授权类型模式的流程中就会包含一个检查，用来确保这个账号是否仍然有效，假如说你禁用了这个账户的话。
         *      - authorizationCodeServices ：这个属性是用来设置授权码服务的（即 AuthorizationCodeServices 的实例对象），主要用于 "authorization_code" 授权码类型模式，此授权码类型模式必须配置。
         *      - implicitGrantService ：这个属性用于设置隐式授权模式，用来管理隐式授权模式的状态。 主要用于 "隐式授权" 授权码类型模式，此授权码类型模式必须配置。
         *      - tokenGranter：当你设置了这个东西（即 TokenGranter 接口实现），那么授权将会交由你来完全掌控，并且会忽略掉上面的这几个属性，这个属性一般是用作拓展用途的，即标准的四种授权模式已经满足不了你的需求的时候，才会考虑使用这个。
         */
        endpoints
                .authenticationManager(authenticationManager)//注入：认证管理器（oAuth2的密码模式需要）
                .authorizationCodeServices(authorizationCodeServices)//注入：授权码服务（oAuth2的授权码模式需要）
                .tokenServices(tokenService())//注入：令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST); //端点允许的请求方式

        /**
         * 配置授权端点的URL（Endpoint URLs）：
         *
         * - AuthorizationServerEndpointsConfigurer 这个配置对象有一个叫做 pathMapping() 的方法用来配置端点URL链接，它有两个参数：
         *      - 第一个参数：String 类型的，这个端点URL的默认链接。
         *      - 第二个参数： String 类型的，你要进行替代的URL链接。
         * - 以上的参数都将以 "/" 字符为开始的字符串，OAuth2.0框架的默认URL链接如下列表（通常不会改变），可以作为这个 pathMapping() 方法的 第一个参数：
         *      - /oauth/authorize：授权端点。
         *      - /oauth/token：令牌端点。
         *      - /oauth/conﬁrm_access ：用户确认授权提交端点。
         *      - /oauth/error ：授权服务错误信息端点。
         *      - /oauth/check_token：用于资源服务访问的令牌解析端点。（资源服务远程来校验token的合法性）
         *      - /oauth/token_key ：提供公有密匙的端点，如果你使用JWT令牌的话。
         *  - 需要注意的是授权端点这个URL应该被Spring Security保护起来只供授权用户访问.
         */
    }

    //设置授权码模式的授权码如何存取，暂时采用内存方式
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 令牌管理服务
     */
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        /**
         * AuthorizationServerTokenServices 接口定义了一些操作使得你可以对令牌进行一些必要的管理，令牌可以被用来加载身份信息，里面包含了这个令牌的相关权限。
         * 自己可以创建 AuthorizationServerTokenServices 这个接口的实现，则需要继承 DefaultTokenServices 这个类， 里面包含了一些有用实例，你可以使用它来修改令牌的格式和令牌的存储。
         * 默认的，当它尝试创建一个令牌的时候，是使用随机值来进行填充的，除了持久化令牌是委托一个 TokenStore 接口来实现以外，这个类（DefaultTokenServices）几乎帮你做了所有的事情。
         * 并且 TokenStore 这个接口有一个默认的实现，它就是 InMemoryTokenStore ，如其命名，所有的 令牌是被保存在了内存中。
         * 除了使用这个类（DefaultTokenServices）以外，你还可以使用一些其他的预定义实现，下面有几个版本，它们都实现了TokenStore接口：
         *      - InMemoryTokenStore：以内存的方式存储令牌。这个版本的实现是被默认采用的，它可以完美的工作在单服务器上（即访问并发量 压力不大的情况下，并且它在失败的时候不会进行备份），大多数的项目都可以使用这个版本的实现来进行 尝试，你可以在开发的时候使用它来进行管理，因为不会被保存到磁盘中，所以更易于调试。
         *      - JdbcTokenStore ：以数据库的方式存储令牌。这是一个基于JDBC的实现版本，令牌会被保存进关系型数据库。使用这个版本的实现时， 你可以在不同的服务器之间共享令牌信息，使用这个版本的时候请注意把"spring-jdbc"这个依赖加入到你的 classpath当中。
         *      - JwtTokenStore ：不存储，jwt携带令牌的信息。这个版本的全称是 JSON Web Token（JWT），它可以把令牌相关的数据进行编码（因此对 于后端服务来说，它不需要进行存储，这将是一个重大优势），但是它有一个缺点，那就是撤销一个已经授 权令牌将会非常困难，所以它通常用来处理一个生命周期较短的令牌以及撤销刷新令牌（refresh_token）。 另外一个缺点就是这个令牌占用的空间会比较大，如果你加入了比较多用户凭证信息。JwtTokenStore 不会保存任何数据，但是它在转换令牌值以及授权信息方面与 DefaultTokenServices 所扮演的角色是一样的。
         */


        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);//客户端详情服务。（实际也就是获得 当前类中 #configure(ClientDetailsServiceConfigurer clients) 中配置内容）
        service.setSupportRefreshToken(true);//是否产生刷新令牌
        service.setTokenStore(tokenStore);//令牌存储策略。（这里的入参 tokenStore ，其实就是TokenConfig类中，实际生效的@Bean方法返回的对应实例。）

        /**
         * uaa step-3
         * - token服务，设置了令牌增强
         * - 完成这步之后，就就可以通过 授权服务uaa生成jwt的令牌了
         *
         */
        //令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;

    }

    /**
     * 令牌端点的安全策略
     *
     * AuthorizationServerSecurityConfigurer：用来配置令牌端点的安全约束.
     *  - 配置针对令牌端点的安全约束（令牌的URL，有哪些人能访问）
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security
                .tokenKeyAccess("permitAll()") // '/oauth/token_key'地址是公开的（tokenKey这个endpoint当使用JwtToken且使用非对称加密时，资源服务用于获取公钥而开放的，这里指这个tokenKey endpoint完全公开。）
                .checkTokenAccess("permitAll()") // '/oauth/check_token'地址是公开的（checkToken这个endpoint完全公开）
                .allowFormAuthenticationForClients() // 允许表单认证，来申请令牌
        ;
    }

}

