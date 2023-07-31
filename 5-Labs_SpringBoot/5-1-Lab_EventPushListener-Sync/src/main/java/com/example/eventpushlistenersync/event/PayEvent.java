package com.example.eventpushlistenersync.event;


import org.springframework.context.ApplicationEvent;

/**
 * @author liuguanshen
 * @create 2023/7/31
 * @description
 *
 * step-1：
 * 定义event事件。在发布订阅，上下游之间传递的事件对象
 *
 * 先定义一个支付成功的事件对象，这个对象要继承spring的ApplicationEvent。
 */
public class PayEvent extends ApplicationEvent {

    /**
     * 入参source，是一个对象
     * 就是上游事件，可以传递给下游事件的一个传递值
     * 在发布事件的时候，放入想要下游接收的对象即可
     */
    public PayEvent(Object source) {
        super(source);
    }
}
