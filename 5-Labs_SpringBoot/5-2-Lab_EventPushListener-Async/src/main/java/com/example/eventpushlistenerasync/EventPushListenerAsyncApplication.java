package com.example.eventpushlistenerasync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 如果需要事件监听者异步执行，需要开启全局Async
 */
@EnableAsync
@SpringBootApplication
public class EventPushListenerAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventPushListenerAsyncApplication.class, args);
    }

    /**
     * 启动项目
     * 请求地址：http://localhost:8080/pay
     *
     * Spring的事件发布监听，默认全是异步的
     * 下游事件监听，是不同线程去处理的
     *
     * 日志记录：
     * 2023-07-31 17:44:18.243  INFO 11313 --- [nio-8080-exec-2] c.e.e.controller.TestController          : 当前线程name=http-nio-8080-exec-2,当前线程id=25 --- 开始支付
     * 2023-07-31 17:44:18.259  INFO 11313 --- [nio-8080-exec-2] c.e.e.controller.TestController          : 当前线程name=http-nio-8080-exec-2,当前线程id=25 --- 支付成功，开始发布事件
     * 2023-07-31 17:44:18.275  INFO 11313 --- [         task-3] c.e.e.eventListener.SmsListener          : 当前线程name=task-3,当前线程id=45 --- 短信服务监听到支付成功事件(上游发送的对象)：支付成功
     * 2023-07-31 17:44:18.275  INFO 11313 --- [         task-3] c.e.e.eventListener.SmsListener          : 当前线程name=task-3,当前线程id=45 --- 开始发送短信
     * 2023-07-31 17:44:18.277  INFO 11313 --- [nio-8080-exec-2] c.e.e.controller.TestController          : 当前线程name=http-nio-8080-exec-2,当前线程id=25 --- 保存支付记录
     * 2023-07-31 17:44:18.278  INFO 11313 --- [         task-4] c.e.e.eventListener.StockListener        : 当前线程name=task-4,当前线程id=46 --- 库存服务监听到支付成功事件(上游发送的对象)：支付成功
     * 2023-07-31 17:44:18.278  INFO 11313 --- [         task-4] c.e.e.eventListener.StockListener        : 当前线程name=task-4,当前线程id=46 --- 开始减少库存
     * 2023-07-31 17:44:20.286  INFO 11313 --- [         task-4] c.e.e.eventListener.StockListener        : 当前线程name=task-4,当前线程id=46 --- 库存减少完毕
     * 2023-07-31 17:44:21.280  INFO 11313 --- [         task-3] c.e.e.eventListener.SmsListener          : 当前线程name=task-3,当前线程id=45 --- 短信发送成功
     */
}
