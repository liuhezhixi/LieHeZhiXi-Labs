package com.example.eventpushlistenersync.eventListener;

import com.example.eventpushlistenersync.event.PayEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author liuguanshen
 * @create 2023/7/31
 * @description step-2：
 * 定义事件监听者（创建 短信的监听者）
 */
@Slf4j
@Component
public class SmsListener {

    /**
     * 事件监听者
     * 下游处理方法
     */
    @EventListener(PayEvent.class)
    public void sendSms(PayEvent payEvent) {
        log.info("当前线程name={},当前线程id={} --- 短信服务监听到支付成功事件(上游发送的对象)：{}",
                Thread.currentThread().getName(), Thread.currentThread().getId(),
                payEvent.getSource());
        try {
            log.info("当前线程name={},当前线程id={} --- 开始发送短信", Thread.currentThread().getName(), Thread.currentThread().getId());
            Thread.sleep(3000);
            log.info("当前线程name={},当前线程id={} --- 短信发送成功", Thread.currentThread().getName(), Thread.currentThread().getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
