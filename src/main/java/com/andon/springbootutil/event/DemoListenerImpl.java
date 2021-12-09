package com.andon.springbootutil.event;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Andon
 * 2021/12/8
 */
@Slf4j
public class DemoListenerImpl implements DemoListener {

    @Override
    public void handleEvent(DemoEvent demoEvent) {
        log.info("handleEvent [{}-{}] >> demoEvent.getObject():{}", Thread.currentThread().getName(), Thread.currentThread().getId(), demoEvent.getObject());
    }
}
