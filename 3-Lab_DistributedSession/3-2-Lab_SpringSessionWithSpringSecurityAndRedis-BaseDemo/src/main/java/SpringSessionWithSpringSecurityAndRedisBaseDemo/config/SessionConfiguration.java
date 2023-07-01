package SpringSessionWithSpringSecurityAndRedisBaseDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * @EnableRedisHttpSession
 * 自动化配置 Spring Session 使用 Redis 作为数据源
 *
 * @EnableRedisHttpSession 该注解有如下属性：
 * - maxInactiveIntervalInSeconds 属性，Session 不活跃后的过期时间，默认为 1800 秒。
 * - redisNamespace 属性，在 Redis 的 key 的统一前缀，默认为 "spring:session" 。
 * - redisFlushMode 属性，Redis 会话刷新模式(RedisFlushMode)。目前有两种，默认为 RedisFlushMode.ON_SAVE 。
 *      - RedisFlushMode.ON_SAVE ，在请求执行完成时，统一写入 Redis 存储。
 *      - RedisFlushMode.IMMEDIATE ，在每次修改 Session 时，立即写入 Redis 存储。
 * - cleanupCron 属性，清理 Redis Session 会话过期的任务执行 CRON 表达式，默认为 "0 * * * * *" 每分钟执行一次。
 *      - 虽然说，Redis 自带了 key 的过期，但是惰性删除策略，实际过期的 Session 还在 Redis 中占用内存。
 *      - 所以，Spring Session 通过定时任务，删除 Redis 中过期的 Session ，尽快释放 Redis 的内存。
 *      - 不了解 Redis 的删除过期 key 的策略的胖友，可以看看 《Redis 中删除过期 Key 的三种策略：定时、定期、惰性》 文章。 https://blog.csdn.net/a_bang/article/details/52986935/
 */
@EnableRedisHttpSession
@Configuration
public class SessionConfiguration {

    /**
     * 相对于3-1-Lab项目的 SessionConfiguration 配置文件来说，去掉了自定义的 JSON RedisSerializer Bean 的配置。
     * 原因是，HttpSession 的 attributes 属性，是 Map<String, Object> 类型。
     * - 在序列化 Session 到 Redis 中时，不存在问题。
     * - 在反序列化 Redis 的 key-value 键值对成 Session 时，如果 attributes 的 value 存在 POJO 对象的时候，因为不知道该 value 是什么 POJO 对象，导致无法反序列化错误。
     * 关于这个问题，胖友可以自己测试下，感受会更加明显。目前，艿艿暂时找不到特别合适的解决方案，所以就换回 Java 序列化方式。
     * 也因此，在使用 Spring Session 时，推荐先老实使用 Java 序列化方式吧。
     */








    /**
     * 7. 自定义 sessionid 的返回设置
     *
     * 在 Spring Session 中，我们可以通过自定义 HttpSessionIdResolver Bean ，设置 sessionid 请求和响应时所在的地方。目前有两种实现，也就是说提供两种策略：
     *  - CookieHttpSessionIdResolver ，sessionid 存放在 Cookie 之中。
     *  - HeaderHttpSessionIdResolver ，sessionid 存放在 Header 之中。
     *
     *  ------------------------------------------------------------------------------------------------------------------------------------------------
     */
    /**
     * 7.1 CookieHttpSessionIdResolver
     * 我们来看看浏览器中，sessionid 在 Cookie 中，是长什么样的？
     *  - 查看路径：浏览器开发模式 - 上方栏选择application(应用程序) - 左下方的选择cookies - 选择当前请求url
     *
     *  我们会看到，默认情况下，Spring Session 产生的 sessionid 的 KEY 为 "SESSION" 。
     *  这是因为 sessionid 在返回给前端时，使用 DefaultCookieSerializer 写回 Cookie 给浏览器，在未自定义 sessionid 的 Cookie 名字的情况下，默认使用 "SESSION" 。
     *  比较神奇的是，sessionid 的 VALUE 竟然看起来是一串加密的字符串？！
     *  😈 在 DefaultCookieSerializer 写回 Cookie 给前端时，会将 sessionid 先 BASE64 编码一下，然后再写回 Cookie 给浏览器。
     *  那么，如果我们想自定义 sessionid 在 Cookie 中，使用别的 KEY 呢，例如说 "JSESSIONID" 。我
     *  们可以通过自定义 CookieHttpSessionIdResolver Bean 来实现。代码如下：
     */
    //@Bean
    //public CookieHttpSessionIdResolver sessionIdResolver() {
    //    // 创建 CookieHttpSessionIdResolver 对象
    //    CookieHttpSessionIdResolver sessionIdResolver = new CookieHttpSessionIdResolver();
    //    // 创建 DefaultCookieSerializer 对象
    //    DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
    //
    //    /**
    //     * 我们可以看到，DefaultCookieSerializer 是 CookieHttpSessionIdResolver 的一个属性，通过它来完成在 Cookie 中的 sessionid 的读取和写入
    //     * 当然，DefaultCookieSerializer 还提供了 Cookie 很多其它的配置选项，胖友可以点击URL查看：https://github.com/spring-projects/spring-session/blob/master/spring-session-core/src/main/java/org/springframework/session/web/http/DefaultCookieSerializer.java
    //     * 例如说：cookieMaxAge 存活时长，domainName 所属域。
    //     */
    //    sessionIdResolver.setCookieSerializer(cookieSerializer); // 设置到 sessionIdResolver 中
    //    cookieSerializer.setCookieName("biaoCookie"); //设置浏览器中cookie的key的名字
    //    cookieSerializer.setUseBase64Encoding(false); //设置浏览器中cookie的value不使用base64编码
    //
    //    return sessionIdResolver;
    //}

    /**
     * 7.2 HeaderHttpSessionIdResolver
     * 当我们希望 session 存放在 Header 之中时，我们可以通过自定义 HeaderHttpSessionIdResolver Bean 来实现。代码如下：
     * todo 设置header暂时没完全成功，感觉会和SpringSecurity产生影响，，自定义的header影响了Security的鉴权，后续研究研究
     *
     * 随便请求一个 API 接口，我们来看看响应的 Header 中，是不是有 "token" 的返回。如下图所示：
     */
    //@Bean
    //public HeaderHttpSessionIdResolver sessionIdResolver() {
    //    //return HeaderHttpSessionIdResolver.xAuthToken();
    //    //return HeaderHttpSessionIdResolver.authenticationInfo();
    //
    //    //创建 HeaderHttpSessionIdResolver 对象，传入 headerName 请求头名。例如说，这里艿艿传入了 "biaoTest" 。
    //    return new HeaderHttpSessionIdResolver("biaoTest");
    //}

}
