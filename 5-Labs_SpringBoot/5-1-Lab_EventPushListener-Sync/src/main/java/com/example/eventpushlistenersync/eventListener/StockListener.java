package com.example.eventpushlistenersync.eventListener;

import com.example.eventpushlistenersync.event.PayEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author liuguanshen
 * @create 2023/7/31
 * @description step-3：定义事件监听者（创建 库存监听者）
 */
@Slf4j
@Component
public class StockListener {

    /**
     * 事件监听者
     * 下游处理方法
     */
    @EventListener(PayEvent.class)
    public void reduceStock(PayEvent payEvent) {
        log.info("当前线程name={},当前线程id={} --- 库存服务监听到支付成功事件(上游发送的对象)：{}",
                Thread.currentThread().getName(), Thread.currentThread().getId(),
                payEvent.getSource());
        try {
            log.info("当前线程name={},当前线程id={} --- 开始减少库存", Thread.currentThread().getName(), Thread.currentThread().getId());
            Thread.sleep(2000);
            log.info("当前线程name={},当前线程id={} --- 库存减少完毕", Thread.currentThread().getName(), Thread.currentThread().getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
