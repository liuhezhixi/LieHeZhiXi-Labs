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
     * 密码模式（演示）
     *  - 在此项目中是：
     *      - 在apiFox中使用post请求：
     *      - http://127.0.0.1:53020/uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=biao&password=biao
     *
     *   - 请求返回结果
     *      - 重点！！！现在是jwt版本的token值，这个jwt版本的token里面就包含了用户的信息和权限了
     *
     *   - {
     *          "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJiaWFvIiwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTY5MTQwNzc0NCwiYXV0aG9yaXRpZXMiOlsicDEiXSwianRpIjoiNGVjYzdkNTctYzVmNS00ZDU1LTlhMTItZWZmOGNkNjAwODAwIiwiY2xpZW50X2lkIjoiYzEifQ.ct8yJNZTEGIhv5QuHErA8Mube_y06y5zubT-4ELZk70",
     *          "token_type": "bearer",
     *          "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJiaWFvIiwic2NvcGUiOlsiYWxsIl0sImF0aSI6IjRlY2M3ZDU3LWM1ZjUtNGQ1NS05YTEyLWVmZjhjZDYwMDgwMCIsImV4cCI6MTY5MTY1OTc0NCwiYXV0aG9yaXRpZXMiOlsicDEiXSwianRpIjoiNTdiNDUxNzMtMzM5Yy00NzdmLWJmMzgtYzRlNjk2NjBjOGI3IiwiY2xpZW50X2lkIjoiYzEifQ.4A53UHfp7Oj_x0fHq5LOIbuLHhlBoZy-In9ZUNVcthE",
     *          "expires_in": 7199,
     *          "scope": "all",
     *          "jti": "4ecc7d57-c5f5-4d55-9a12-eff8cd600800"
     *     }
     */

}
