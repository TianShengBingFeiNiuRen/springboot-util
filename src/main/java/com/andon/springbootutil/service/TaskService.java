package com.andon.springbootutil.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
@Service
public class TaskService implements Runnable {

    @Override
    public void run() {
        // 定时任务业务逻辑
        log.info("taskService run!!");
    }
}
