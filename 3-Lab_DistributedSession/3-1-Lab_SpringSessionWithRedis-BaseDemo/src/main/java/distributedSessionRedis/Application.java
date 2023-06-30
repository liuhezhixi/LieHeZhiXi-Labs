package distributedSessionRedis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * ① 在浏览器中，访问 "http://127.0.0.1:8080/session/get" 接口，返回目前的 Session 的内容。响应结果如下：
     * {}
     * 空空的Json，这也符合期望。
     * ----------------------------
     * 在 redis-cli 中：
     *      127.0.0.1:6379> scan 0
     *      1) "0"
     * ----------------------------
     *
     */

    /**
     * ②
     * ------------------------------------------------------------------------------------
     * 再次在 redis-cli 中：
     *      127.0.0.1:6379> scan 0
     *      1) "0"
     *      2) 1) "spring:session:sessions:42987055-6dd4-4b98-bbf2-f8507b757284"
     *         2) "spring:session:expirations:1688110620000"
     *         3) "spring:session:sessions:expires:42987055-6dd4-4b98-bbf2-f8507b757284"
     *
     * ------------------------------------------------------------------------------------
     * 每一个 Session 对应 Redis 二个 key-value 键值对。
     *  - 两个"spring:session:sessions" 和 两个"spring:session:sessions:expires"
     * 而 "spring:session:expirations:{时间戳}" ，是为了获得每分钟需要过期的 sessionid 集合，即 {时间戳} 是每分钟的时间戳。
     *
     *  - 开头：以 spring:session 开头，可以通过 @EnableRedisHttpSession 注解的 redisNamespace 属性配置。
     *  - 结尾：以对应 Session 的 sessionid 结尾。
     *  - 中间：中间分别是 "session"、"expirations"、sessions:expires 。
     *      - **一般情况下，我们只需要关注中间为 "session" 的 key-value 键值对即可，它负责真正存储 Session 数据。
     *      - **对于中间为 "sessions:expires" 和 "expirations" 的两个来说，主要为了实现主动删除 Redis 过期的 Session 会话，解决 Redis 惰性删除的“问题”。具体的实现原理，本文就不赘述，感兴趣的胖友，可以看看 《从 Spring-Session 源码看 Session 机制的实现细节》 文章。https://www.iocoder.cn/Spring-Session/laoxu/spring-session-4/?self
     *
     * ------------------------------------------------------------------------------------
     * 此时我们还没有新增keyValue的session值进来
     * 我们查看一个"spring:session:sessions"数据，它是一个 Redis hash 数据结构。结果如下：
     *
     *  127.0.0.1:6379> HGETALL spring:session:sessions:42987055-6dd4-4b98-bbf2-f8507b757284
     *  1) "lastAccessedTime" # 最后访问时间
     *  2) "1688106316084" # 最后访问时间的value
     *  3) "maxInactiveInterval" # Session 允许最大不活跃时长，单位：秒。
     *  4) "1800" # Session 允许最大不活跃时长的value
     *  5) "creationTime" # 创建时间
     *  6) "1688106316084" # 创建时间的value
     *
     *  127.0.0.1:6379> ttl spring:session:sessions:42987055-6dd4-4b98-bbf2-f8507b757284
     *  (integer) 1876 # 虽然说，Spring Session Redis 实现了主动删除，但是并不妨碍这里也使用 Redis 自动过期策略。
     *
     *  ------------------------------------------------------------------------------------
     *
     * 在浏览器中，访问
     *      * "http://127.0.0.1:8080/session/set?key=keybiao1&value=valuebiao1"
     *      * "http://127.0.0.1:8080/session/set?key=keybiao2&value=valuebiao2"
     *      * 接口，设置两个 key-value 键值对。
     *
     *  127.0.0.1:6379> HGETALL spring:session:sessions:42987055-6dd4-4b98-bbf2-f8507b757284
     *  1) "lastAccessedTime"
     *  2) "1688109298658"
     *  3) "maxInactiveInterval"
     *  4) "1800"
     *  5) "creationTime"
     *  6) "1688108814268"
     *  7) "sessionAttr:keybiao1"
     *  8) "\"valuebiao1\""
     *  9) "sessionAttr:keybiao2"
     * 10) "\"valuebiao2\""
     * 我们调用 HttpSession#setAttribute(String name, Object value) 方法，
     * 设置的每一个 key-value 键值对，对应到 Redis hash 数据结构中的一个 key 。
     * 考虑到毕竟 key 冲突，使用 "sessionAttr:" 开头。
     */

}
