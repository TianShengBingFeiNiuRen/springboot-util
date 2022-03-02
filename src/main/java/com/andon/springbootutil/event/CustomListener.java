package com.andon.springbootutil.event;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Andon
 * 2022/3/1
 * <p>
 * 自定义监听
 */
@Slf4j
@Component
public class CustomListener implements ApplicationListener<CustomEvent> {

    @SneakyThrows
    @Async("globalExecutorService") //异步，指定线程池
    @Override
    public void onApplicationEvent(CustomEvent event) {
        log.info("CustomListener onApplicationEvent start!! [{}] event.source:{} event.id:{} event.type:{}", Thread.currentThread().getId(), event.getSource(), event.getId(), event.getType());
        Thread.sleep(3000);
        log.info("CustomListener onApplicationEvent end!!");
    }
}
