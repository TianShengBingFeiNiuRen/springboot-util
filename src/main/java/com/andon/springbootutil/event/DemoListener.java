package com.andon.springbootutil.event;

import java.util.EventListener;

/**
 * @author Andon
 * 2021/12/8
 * <p>
 * 监听器接口
 */
public interface DemoListener extends EventListener {

    void handleEvent(DemoEvent demoEvent);
}
