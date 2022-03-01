package com.andon.springbootutil.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Andon
 * 2022/3/1
 * <p>
 * 自定义事件
 */
@Getter
public class CustomEvent extends ApplicationEvent {

    private final String id;
    private final String type;

    public CustomEvent(Object source, String id, String type) {
        super(source);
        this.id = id;
        this.type = type;
    }
}
