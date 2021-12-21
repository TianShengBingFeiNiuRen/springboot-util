package com.andon.springbootutil.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Andon
 * 2021/12/8
 */
@Slf4j
@SpringBootConfiguration
public class ExecutorConfig {

    @Bean
    public ThreadPoolExecutor globalExecutorService() {
        String osName = System.getProperty("os.name");
        log.info("osName:{}", osName);
        //获取当前机器的核数
        int cpuNum = Runtime.getRuntime().availableProcessors();
        log.info("cpuNum:{}", cpuNum);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                cpuNum, cpuNum * 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        log.info("executor:{}", JSONObject.toJSONString(executor));
        return executor;
    }
}
