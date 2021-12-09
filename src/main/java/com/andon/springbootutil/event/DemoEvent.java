package com.andon.springbootutil.event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.EventObject;

/**
 * @author Andon
 * 2021/12/8
 * <p>
 * 定义事件
 */
@Slf4j
public class DemoEvent extends EventObject {

    private Object object;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DemoEvent(Object source) {
        super(source);
    }

    public Object getObject() {
        return object;
    }
}
