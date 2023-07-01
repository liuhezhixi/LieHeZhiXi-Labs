package SpringSessionWithSpringSecurityAndRedisBaseDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //首先情况redis数据库的数据，避免脏数据出现

    /**
     * ①
     * 先进入redis-cli，执行 scan 0 保证没有脏数据
     * 127.0.0.1:6379> scan 0
     * 1) "0"
     * 2) (empty array)
     * ------------------------------------------------------------------------
     *
     * 在浏览器中，访问 "http://127.0.0.1:8080/" 地址
     * 因为 使用了 Spring Security ，所以会被自动拦截重定向到 "http://127.0.0.1:8080/login" 登录地址。
     *
     * 然后再查看 Redis 中，查看是否创建了一个 Session
     * # 确实创建了一个 sessionid 为 65ae2fd7-ab57-4446-ae64-7d1d2105b8ca。
     *
     * 127.0.0.1:6379> scan 0
     * 1) "0"
     * 2) 1) "spring:session:sessions:expires:65ae2fd7-ab57-4446-ae64-7d1d2105b8ca"
     *    2) "spring:session:expirations:1688177460000"
     *    3) "spring:session:sessions:65ae2fd7-ab57-4446-ae64-7d1d2105b8ca"
     *
     * ------------------------------------------------------------------------------------------------------------------------------------------------
     *
     * # 查看 spring:session:sessions:65ae2fd7-ab57-4446-ae64-7d1d2105b8ca 存储的 value 。
     * # 因为使用 Java 序列化方式，所以 hash 中的每个 key 对应的 value ，都无法直接读懂。不过大概啥意思，我们应该是明白的。
     *
     * 127.0.0.1:6379> HGETALL spring:session:sessions:65ae2fd7-ab57-4446-ae64-7d1d2105b8ca
     *  1) "lastAccessedTime"
     *  2) "\xac\xed\x00\x05sr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x01n\x93\xb5\x80\xf6"
     *  3) "creationTime"
     *  4) "\xac\xed\x00\x05sr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x01n\x93\xb5\x80x"
     *  5) "sessionAttr:SPRING_SECURITY_SAVED_REQUEST"
     *  6) "\xac\xed\x00\x05sr\x00Aorg.springframework.security.web.savedrequest.DefaultSavedRequest\x1e@HD\xf96d\x94\x02\x00\x0eI\x00\nserverPortL\x00\x0bcontextPatht\x00\x12Ljava/lang/String;L\x00\acookiest\x00\x15Ljava/util/ArrayList;L\x00\aheaderst\x00\x0fLjava/util/Map;L\x00\alocalesq\x00~\x00\x02L\x00\x06methodq\x00~\x00\x01L\x00\nparametersq\x00~\x00\x03L\x00\bpathInfoq\x00~\x00\x01L\x00\x0bqueryStringq\x00~\x00\x01L\x00\nrequestURIq\x00~\x00\x01L\x00\nrequestURLq\x00~\x00\x01L\x00\x06schemeq\x00~\x00\x01L\x00\nserverNameq\x00~\x00\x01L\x00\x0bservletPathq\x00~\x00\x01xp\x00\x00\x1f\x90t\x00\x00sr\x00\x13java.util.ArrayListx\x81\xd2\x1d\x99\xc7a\x9d\x03\x00\x01I\x00\x04sizexp\x00\x00\x00\x01w\x04\x00\x00\x00\x01sr\x009org.springframework.security.web.savedrequest.SavedCookie\x03@+\x82\x9f\xc0\xb4f\x02\x00\bI\x00\x06maxAgeZ\x00\x06secureI\x00\aversionL\x00\acommentq\x00~\x00\x01L\x00\x06domainq\x00~\x00\x01L\x00\x04nameq\x00~\x00\x01L\x00\x04pathq\x00~\x00\x01L\x00\x05valueq\x00~\x00\x01xp\xff\xff\xff\xff\x00\x00\x00\x00\x00ppt\x00\nJSESSIONIDpt\x00 93C9186722D346CD142D62FA28085693xsr\x00\x11java.util.TreeMap\x0c\xc1\xf6>-%j\xe6\x03\x00\x01L\x00\ncomparatort\x00\x16Ljava/util/Comparator;xpsr\x00*java.lang.String$CaseInsensitiveComparatorw\x03\\}\\P\xe5\xce\x02\x00\x00xpw\x04\x00\x00\x00\x0ft\x00\x06acceptsq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00|text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,/q=0.8,application/signed-exchange;v=b3;q=0.7xt\x00\x0faccept-encodingsq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\x11gzip, deflate, brxt\x00\x0faccept-languagesq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00/zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6xt\x00\nconnectionsq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\nkeep-alivext\x00\x06cookiesq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00+JSESSIONID=93C9186722D346CD142D62FA28085693xt\x00\x04hostsq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\x0e127.0.0.1:8080xt\x00\tsec-ch-uasq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00A\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Microsoft Edge\";v=\"114\"xt\x00\x10sec-ch-ua-mobilesq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\x02?0xt\x00\x12sec-ch-ua-platformsq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\a\"macOS\"xt\x00\x0esec-fetch-destsq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\bdocumentxt\x00\x0esec-fetch-modesq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\bnavigatext\x00\x0esec-fetch-sitesq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\x04nonext\x00\x0esec-fetch-usersq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\x02?1xt\x00\x19upgrade-insecure-requestssq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\x011xt\x00\nuser-agentsq\x00~\x00\x06\x00\x00\x00\x01w\x04\x00\x00\x00\x01t\x00\x87Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.67xxsq\x00~\x00\x06\x00\x00\x00\x05w\x04\x00\x00\x00\x05sr\x00\x10java.util.Locale~\xf8\x11`\x9c0\xf9\xec\x03\x00\x06I\x00\bhashcodeL\x00\acountryq\x00~\x00\x01L\x00\nextensionsq\x00~\x00\x01L\x00\blanguageq\x00~\x00\x01L\x00\x06scriptq\x00~\x00\x01L\x00\avariantq\x00~\x00\x01xp\xff\xff\xff\xfft\x00\x02CNq\x00~\x00\x05t\x00\x02zhq\x00~\x00\x05q\x00~\x00\x05xsq\x00~\x00?\xff\xff\xff\xffq\x00~\x00\x05q\x00~\x00\x05q\x00~\x00Bq\x00~\x00\x05q\x00~\x00\x05xsq\x00~\x00?\xff\xff\xff\xffq\x00~\x00\x05q\x00~\x00\x05t\x00\x02enq\x00~\x00\x05q\x00~\x00\x05xsq\x00~\x00?\xff\xff\xff\xfft\x00\x02GBq\x00~\x00\x05q\x00~\x00Eq\x00~\x00\x05q\x00~\x00\x05xsq\x00~\x00?\xff\xff\xff\xfft\x00\x02USq\x00~\x00\x05q\x00~\x00Eq\x00~\x00\x05q\x00~\x00\x05xxt\x00\x03GETsq\x00~\x00\x0cpw\x04\x00\x00\x00\x00xppt\x00\x01/t\x00\x16http://127.0.0.1:8080/t\x00\x04httpt\x00\t127.0.0.1t\x00\x01/"
     *  7) "maxInactiveInterval"
     *  8) "\xac\xed\x00\x05sr\x00\x11java.lang.Integer\x12\xe2\xa0\xa4\xf7\x81\x878\x02\x00\x01I\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\a\b"
     *  9) "sessionAttr:org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN"
     * 10) "\xac\xed\x00\x05sr\x006org.springframework.security.web.csrf.DefaultCsrfTokenZ\xef\xb7\xc8/\xa2\xfb\xd5\x02\x00\x03L\x00\nheaderNamet\x00\x12Ljava/lang/String;L\x00\rparameterNameq\x00~\x00\x01L\x00\x05tokenq\x00~\x00\x01xpt\x00\x0cX-CSRF-TOKENt\x00\x05_csrft\x00$582ef040-9a9f-43be-9708-e5b94394e338"
     *
     * ------------------------------------------------------------------------------------------------------------------------------------------------
     *
     */

    /**
     * ② 在浏览器中，输入账号密码，并点击「Sign in」按钮，进行登录。登录成功后
     * 在 Redis 的终端中，查看此时该 Session 的变化。如下：
     *
     * 127.0.0.1:6379> scan 0
     * 1) "0"
     * 2) 1) "spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:admin"
     *    2) "spring:session:expirations:1688177460000"
     *    3) "spring:session:sessions:expires:b5e5a8be-6ded-4e20-b46d-896752ceedda"
     *    4) "spring:session:sessions:b5e5a8be-6ded-4e20-b46d-896752ceedda"
     *
     * # 相比原来没登陆之前来说，多了一条 "spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:admin"
     * # 它代表的，就是我们刚登录的用户名，注意结尾是 "admin" 。
     *
     * # 并且，比较神奇的是，原 sessionid="65ae2fd7-ab57-4446-ae64-7d1d2105b8ca" 的 Session 被删除。
     * # 同时，一个新的 sessionid="b5e5a8be-6ded-4e20-b46d-896752ceedda" 的 Session 被创建
     * # 原因可见 https://www.jianshu.com/p/057fcf061b94 文章的「Spring Security 固定 Session 的保护」。
     *
     * ------------------------------------------------------------------------------------------------------------------------------------------------
     *
     * 如果我们淡出查询这条多出来的数据
     * 127.0.0.1:6379> SMEMBERS spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:admin
     * 1) "\xac\xed\x00\x05t\x00$7da33519-4378-4129-9a64-bf5bed7861a1"
     *
     * # 该 key 是 Redis Set 数据结构，存储了该用户名对应的所有 sessionid 集合。
     * # 所以，通过该 key ，我们可以获取到指定用户名，登录的所有 sessionid 集合
     * # 具体怎么操作，可以参考 本项目中的 SessionController.class 里的「5.6 FindByIndexNameSessionRepository」小节来看。
     *
     * ------------------------------------------------------------------------------------------------------------------------------------------------
     */

    /**
     * ③ 在浏览器中，访问 "http://127.0.0.1:8080/logout" 地址，因为使用了 Spring Security ，所以内置了该登出（退出）界面。
     * 点击「Log Out」按钮，完成用户的登出操作。完成后，在 Redis 的终端中，查看此时该 Session 的变化。
     *      - 注意这里一定要出现 「Log Out」按钮！在这个demo环境下，最好不要修改spring Security的配置
     *      - todo 这里后续可以研究下，如何配置SpringSecurity，才能实现老sessionId不被删除，并且新增一个新的sessionId
     *
     * 127.0.0.1:6379> scan 0
     * 1) "0"
     * 2) 1) "spring:session:expirations:1688177460000"
     *    2) "spring:session:sessions:663b02f1-7a98-4e10-8e9a-32aea4b5cd67"
     *    3) "spring:session:sessions:b5e5a8be-6ded-4e20-b46d-896752ceedda"
     *    4) "spring:session:sessions:expires:663b02f1-7a98-4e10-8e9a-32aea4b5cd67"
     *
     * # 用户退出后，"spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:yudaoyuanma" 被删除
     * # 因为，该用户已经退出。
     *
     * # 用户退出后，新的 sessionid="663b02f1-7a98-4e10-8e9a-32aea4b5cd67" 的 Session 被创建。
     *
     * # 神奇的是，老的 sessionid="b5e5a8be-6ded-4e20-b46d-896752ceedda"" 的 Session 并未被删除，这是为什么呢？
     * # Spring Session 在失效删除 Session 时，会保留该 sessionid 300 秒。对应源码为 RedisSessionExpirationPolicy#onExpirationUpdated(...) 方法的最后一行。
     * # 具体原因，是为了实现 Session 过期的通知。
     * # 详细的，可以看 《从 Spring-Session 源码看 Session 机制的实现细节》 http://www.iocoder.cn/Spring-Session/laoxu/spring-session-4/?self
     *
     * 至此，我们已经完成 Spring Session + Spring Security 的整合。整个过程，胖友最好自己操作一遍，也要看看 Redis 里的 Session 变化噢。
     */
}
