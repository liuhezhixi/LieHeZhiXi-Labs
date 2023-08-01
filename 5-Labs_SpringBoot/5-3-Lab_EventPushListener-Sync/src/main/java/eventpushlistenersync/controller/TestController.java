package eventpushlistenersync.controller;


import eventpushlistenersync.event.PayEvent;
import eventpushlistenersync.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liuguanshen
 * @create 2023/7/31
 * @description
 */
@Slf4j
@Controller
public class TestController {

    @GetMapping("/pay")
    public void pay() {
        log.info("当前线程name={},当前线程id={} --- 开始支付",Thread.currentThread().getName(),Thread.currentThread().getId());
        log.info("当前线程name={},当前线程id={} --- 支付成功，开始发布事件",Thread.currentThread().getName(),Thread.currentThread().getId());
        SpringContextHolder.publishEvent(new PayEvent("支付成功"));
        log.info("当前线程name={},当前线程id={} --- 保存支付记录",Thread.currentThread().getName(),Thread.currentThread().getId());
    }
}
