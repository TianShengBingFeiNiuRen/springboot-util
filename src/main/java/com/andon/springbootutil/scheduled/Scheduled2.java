package com.andon.springbootutil.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Andon
 * 2022/8/10
 */
@Slf4j
@Component
public class Scheduled2 {

    @Scheduled(initialDelay = 5, fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void scheduled2() {
        log.info("scheduled2 run!!");
    }
}
