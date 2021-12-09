package com.andon.springbootutil.event;

/**
 * @author Andon
 * 2021/12/8
 */
public class EventTest {

    public static void main(String[] args) {
        //定义事件源管理实践
        DemoSource eventSource = new DemoSource();
        DemoListener listener = new DemoListenerImpl();

        // 注册监听，往事件源添加监听
        eventSource.addEventListener(listener);

        // 模拟事件触发
        DemoEvent demoEvent = new DemoEvent("HelloWorld");
        // 用事件源激活事件，最终执行的是监听实现类里的@Override方法
        eventSource.fireEvent(demoEvent);
    }
}
