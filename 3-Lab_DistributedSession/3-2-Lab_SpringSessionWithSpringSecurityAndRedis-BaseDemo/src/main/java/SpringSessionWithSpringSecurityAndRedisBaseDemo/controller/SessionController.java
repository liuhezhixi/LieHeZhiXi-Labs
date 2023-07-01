package SpringSessionWithSpringSecurityAndRedisBaseDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {


    @PostMapping("/loginSuccess")
    public String loginSuccess() {
        return "登陆成功！！自动跳转过来的页面";
    }


    @GetMapping("/get_all")
    public Map<String, Object> getAll(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        // 遍历
        for (Enumeration<String> enumeration = session.getAttributeNames();
             enumeration.hasMoreElements(); ) {
            String key = enumeration.nextElement();
            Object value = session.getAttribute(key);
            result.put(key, value);
        }
        // 返回
        return result;
    }


    /**
     * 5.6 FindByIndexNameSessionRepository
     *
     * Spring Session 定义了 org.springframework.session.FindByIndexNameSessionRepository 接口，定义了根据用户名，查询登录的所有 Session 信息。
     * 进入 FindByIndexNameSessionRepository.class，对应信息如下
     *      - #findByPrincipalName(String principalName) 方法，根据用户名，查询登录的所有 Session 信息。因为用户可以多点登录，所以一个用户名会对应多个 Session 信息。而返回的 Map 对象，其 key 为 sessionid 。
     *      - #findByPrincipalName(String principalName) 方法的内部，会调用 #findByIndexNameAndIndexValue(indexName, indexValue) 方法，查询指定 indexName 的 value 等于 indexValue 的所有 Session 信息。
     *        这里，传入的是 indexName 为 PRINCIPAL_NAME_INDEX_NAME ，indexValue 为用户名。是不是有点懵逼？
     *        让我们回过头看看 "spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:admin" 这个 Redis key 。分解如下：
     *          - 第一部分，"spring:session:index" 为 index 固定前缀。
     *          - 第二部分，"org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME" 为 indexName 。
     *          - 第三部分，"admin" 为 indexValue 用户名。
     *          - 这样，在 #findByIndexNameAndIndexValue(indexName, indexValue) 方法的内部实现，会将三个部分拼接在一起，
     *            成为 "spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:admin" 字符串，最终去 Redis 查询。
     *            也就是说，第一部分的"spring:session:index" 的目的，是实现索引。
     *            在这里，就是索引用户登录的多个 sessionid ，从而最终查询到所有 Session 信息。
     */
    @Autowired
    private FindByIndexNameSessionRepository sessionRepository;


    /**
     * 首先，在两个浏览器中，登录用户名为 "admin" 的用户。
     * 然后，浏览器访问 "http://127.0.0.1:8080/session/list?username=admin" 接口，查询用户名为 "admin" 的所有 Session 信息。响应结果如下：
     *
     * {
     *     "99cd508f-60b2-4c97-9de2-0c3085e45e1e": {
     *         "new": false,
     *         "lastAccessedTime": "2023-07-01T02:17:39.322Z",
     *         "expired": false,
     *         "attributeNames": [
     *             "SPRING_SECURITY_CONTEXT"
     *         ],
     *         "maxInactiveInterval": "PT30M",
     *         "id": "99cd508f-60b2-4c97-9de2-0c3085e45e1e",
     *         "creationTime": "2023-07-01T02:15:34.706Z"
     *     },
     *     "1cb4673b-110b-4328-b33c-098a82b04bdb": {
     *         "new": false,
     *         "lastAccessedTime": "2023-07-01T02:17:02.420Z",
     *         "expired": false,
     *         "attributeNames": [
     *             "SPRING_SECURITY_CONTEXT"
     *         ],
     *         "maxInactiveInterval": "PT30M",
     *         "id": "1cb4673b-110b-4328-b33c-098a82b04bdb",
     *         "creationTime": "2023-07-01T01:40:59.848Z"
     *     }
     * }
     *
     * 可以看到我们在两个浏览器上，登录的两个 Session
     */
    @GetMapping("/list")
    public Map<String, ? extends Session> list(@RequestParam("username") String username) {
        return sessionRepository.findByPrincipalName(username);
    }

}
