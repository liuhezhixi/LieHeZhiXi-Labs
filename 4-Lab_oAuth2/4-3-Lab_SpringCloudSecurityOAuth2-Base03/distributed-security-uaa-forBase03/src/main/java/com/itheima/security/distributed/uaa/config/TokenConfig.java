package com.itheima.security.distributed.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
public class TokenConfig {

    /**
     * uaa step-1
     * - 设置对称密钥（uua和order是相同的，生产环境使用Nacos读取）
     */
    private String SIGNING_KEY = "uaa123";


    /**
     * uaa step-2
     * - 在授权服务uaa 和 资源服务order中，都使用jwt来传输令牌，减少每次order需要校验令牌都来uaa请求校验的系统开销。
     * - 改用jwt，来 储存 和 校验 OAuth颁发的token
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        //JWT令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
        return converter;
    }

    /**
     * 令牌存储策略
     * 使用内存存储令牌（普通令牌）
     *
     * 该项目，基础内存储存token的方式pass了，改用了jwt
     */
/*    @Bean
    public TokenStore tokenStore() {
        //使用内存存储令牌（普通令牌）
        return new InMemoryTokenStore();
    }*/
}
