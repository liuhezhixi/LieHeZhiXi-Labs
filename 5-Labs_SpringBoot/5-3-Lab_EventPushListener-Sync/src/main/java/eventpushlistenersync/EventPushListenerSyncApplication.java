package eventpushlistenersync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventPushListenerSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventPushListenerSyncApplication.class, args);
    }

    /**
     * 启动项目
     * 请求地址：http://localhost:8080/pay
     *
     * Spring的事件发布监听，默认全是同步的
     *
     * 日志记录：
     * 2023-07-31 17:11:16.101  INFO 10427 --- [nio-8080-exec-1] c.e.e.controller.TestController          : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 开始支付
     * 2023-07-31 17:11:16.103  INFO 10427 --- [nio-8080-exec-1] c.e.e.controller.TestController          : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 支付成功，开始发布事件
     * 2023-07-31 17:11:16.103  INFO 10427 --- [nio-8080-exec-1] c.e.e.eventListener.SmsListener          : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 短信服务监听到支付成功事件(上游发送的对象)：支付成功
     * 2023-07-31 17:11:16.103  INFO 10427 --- [nio-8080-exec-1] c.e.e.eventListener.SmsListener          : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 开始发送短信
     * 2023-07-31 17:11:19.107  INFO 10427 --- [nio-8080-exec-1] c.e.e.eventListener.SmsListener          : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 短信发送成功
     * 2023-07-31 17:11:19.108  INFO 10427 --- [nio-8080-exec-1] c.e.e.eventListener.StockListener        : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 库存服务监听到支付成功事件(上游发送的对象)：支付成功
     * 2023-07-31 17:11:19.108  INFO 10427 --- [nio-8080-exec-1] c.e.e.eventListener.StockListener        : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 开始减少库存
     * 2023-07-31 17:11:21.112  INFO 10427 --- [nio-8080-exec-1] c.e.e.eventListener.StockListener        : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 库存减少完毕
     * 2023-07-31 17:11:21.113  INFO 10427 --- [nio-8080-exec-1] c.e.e.controller.TestController          : 当前线程name=http-nio-8080-exec-1,当前线程id=25 --- 保存支付记录
     *
     */

}
