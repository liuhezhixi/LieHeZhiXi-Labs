package com.itheima.security.distributed.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author liuguanshen
 * @create 2023/8/7
 * @description
 */


/**
 * 配置资源服务器
 *
 * @EnableResourceServer 注解到一个 @Conﬁguration 配置类上，标注当前服务是OAuth2的资源服务，并且必须使用 ResourceServerConfigurer 这个配置对象来进行配置（可以选择继承自 ResourceServerConfigurerAdapter 然后覆写其中的方法，参数就是这个对象的实例）
 * 下面是一些可以配置的属性：
 *  - ResourceServerConfigurer中主要包括：
 *      - tokenServices ：ResourceServerTokenServices 类的实例，用来实现令牌服务。
 *      - tokenStore：TokenStore类的实例，指定令牌如何访问，与tokenServices配置可选
 *      - resourceId：这个资源服务的ID，这个属性是可选的，但是推荐设置并在授权服务中进行验证。 其他的拓展属性例如
 *      - tokenExtractor 令牌提取器用来提取请求中的令牌。
 *
 * @EnableResourceServer 注解自动增加了一个类型为 OAuth2AuthenticationProcessingFilter 的过滤器链
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    @Autowired
    TokenStore tokenStore;

    /**
     * 配置 该资源服务的资源id
     *
     * 这个id在授权服务uaa端，配置的 .resourceIds(...)（客户端可以访问的资源列表）是相互对应的。
     */
    public static final String RESOURCE_ID = "res1";


    /**
     * 配置 资源服务信息
     * @param resources
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        /**
         * order step-2
         * - 修改资源服务端的token验证方式
         *  - 从以前的每次请求uaa验证token，到现在本地解析jwt获取jwt里面包含的信息
         */
        resources.resourceId(RESOURCE_ID)//资源 id
                //.tokenServices(tokenService())//屏蔽原来的，验证令牌的服务
                .tokenStore(tokenStore) //使用jwt的token验证方式
                .stateless(true);
    }


    /**
     * 资源服务令牌解析服务
     *
     * ResourceServerTokenServices 是组成授权服务的另一半
     * 如果你的授权服务和资源服务在同一个应用程序上的话，你可以使用 DefaultTokenServices ，这样的话，你就不用考虑关于实现所有必要的接口的一致性问题。
     * 如果你的资源服务器是分离开的，那么你就必须要确保能够有匹配授权服务提供的 ResourceServerTokenServices，它才知道如何对令牌进行解码。
     *
     * 令牌解析方法：
     * - 使用 DefaultTokenServices 在资源服务器本地配置令牌存储、解码、解析方式
     * - 使用 RemoteTokenServices 资源服务器通过 HTTP 请求来解码令牌，每次都请求授权服务器端点 /oauth/check_token
     *      - 使用授权服务的 /oauth/check_token 端点你需要在授权服务将这个端点暴露出去，以便资源服务可以进行访问
     *      - 这在咱们授权服务配置中已经提到了，可以参考资源服务uaa中的 [令牌端点的安全策略]的配置
     *      - 我们在授权服务uaa中配置了 /oauth/check_token 和 /oauth/token_key 这两个端点是公开的
     */
    @Bean
    public ResourceServerTokenServices tokenService() {
        //使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
        RemoteTokenServices service = new RemoteTokenServices();
        service.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token"); //指定资源服务uaa地址，去校验token
        service.setClientId("c1"); //客户端id
        service.setClientSecret("secret"); //客户端密码
        return service;
    }


    /**
     * 配置 安全访问策略
     *
     * HttpSecurity配置这个与Spring Security类似：
     *  - 请求匹配器，用来设置需要进行保护的资源路径，默认的情况下是保护资源服务的全部路径。
     *  - 通过http.authorizeRequests()来设置受保护资源的访问规则
     *  - 其他的自定义权限保护规则通过 HttpSecurity 来进行配置。
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests()
            .antMatchers("/**")
            .access("#oauth2.hasScope('all')") //这个scope在授权服务uaa端，配置的 .scope(...)（允许的授权范围）是相互对应的。如果授权服务端uaa配置了这个"all"，在资源服务端验证有"all"这个scope范围，请求才会通过校验生效。
            //.access("#oauth2.hasScope('ROLE_ADMIN')")
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //会话控制：因为目前基于token控制会话，所以这里关闭了session。（若使用stateless，则说明Spring Security对登录成功的用户不会创建Session了，你的应用程序也不会允许新建 session。并且它会暗示不使用cookie，所以每个请求都需要重新进行身份验证。这种无状态架构适用于REST API 及其无状态认证机制。）
    }
}
