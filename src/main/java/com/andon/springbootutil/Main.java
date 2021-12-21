package com.andon.springbootutil;

import com.andon.springbootutil.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
@Component
public class Main implements ApplicationRunner {

    public static final ThreadLocal<SimpleDateFormat> FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static final ThreadLocal<SimpleDateFormat> FORMAT_DAY = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    private ScheduledExecutorService scheduledExecutorService; //定时任务线程池

    @Value("${task.time_clear_sector}")
    private String time_clear_sector; //任务执行时间
    @Resource
    private TaskService taskService; //定时任务

    @PostConstruct
    public void init() {
        // 创建线程池
        // scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService = Executors.newScheduledThreadPool(10);
    }

    @Override
    public void run(ApplicationArguments args) {
        for (int i = 0; i < 6; i++) {
            System.out.println("Run!!");
        }

        long initialDelay = 12 * 60 * 60L; //定时任务延时启动
        long period = 24 * 60 * 60L; //定时任务时间间隔

        // 项目启动后，计算定时任务延时的启动时间
        long currentTimeMillis = System.currentTimeMillis();
        String taskTime = FORMAT_DAY.get().format(currentTimeMillis) + " " + time_clear_sector;
        try {
            long taskTimestamp = FORMAT.get().parse(taskTime).getTime();
            initialDelay = (taskTimestamp - currentTimeMillis) > 0 ? (taskTimestamp - currentTimeMillis) / 1000 : 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 定时任务启动（延时时间后周期执行）
        scheduledExecutorService.scheduleAtFixedRate(taskService, initialDelay, period, TimeUnit.SECONDS);
    }
}
