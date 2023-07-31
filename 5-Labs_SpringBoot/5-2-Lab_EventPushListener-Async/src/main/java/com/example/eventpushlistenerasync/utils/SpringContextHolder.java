package com.example.eventpushlistenerasync.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author liuguanshen
 * @create 2023/7/31
 * @description
 *
 * step-4：定义事件发布工具 SpringContextHolder
 * - 我们需要借助ApplicationContext来发布事件。而ApplicationContext需要通过实现ApplicationContextAware来获取。
 * - ApplicationContext继承自ApplicationEventPublisher，通过调用ApplicationEventPublisher的publishEvent()方法，来实现发布事件的目的。
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 发布事件
     * @param applicationEvent
     */
    public static void publishEvent(ApplicationEvent applicationEvent){
        if (applicationEvent != null) {
            applicationContext.publishEvent(applicationEvent);
        }
    }
}
