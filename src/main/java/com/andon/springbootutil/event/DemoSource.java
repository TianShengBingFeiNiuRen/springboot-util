package com.andon.springbootutil.event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andon
 * 2021/12/8
 * <p>
 * 事件源
 */
public class DemoSource {

    private static List<DemoListener> demoListenerList = new ArrayList<>();

    public DemoSource() {
    }

    /**
     * 添加一个监听
     */
    public void addEventListener(DemoListener demoListener) {
        demoListenerList.add(demoListener);
    }

    /**
     * 删除一个监听
     */
    public void removeEventListener(DemoListener demoListener) {
        demoListenerList.remove(demoListener);
    }

    /**
     * 激活监听事件
     */
    public void fireEvent(DemoEvent event) {
        for (DemoListener demoListener : demoListenerList) {
            demoListener.handleEvent(event);
        }
    }
}
