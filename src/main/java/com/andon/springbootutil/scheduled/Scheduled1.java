package com.andon.springbootutil.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Andon
 * 2022/8/10
 */
@Slf4j
@Component
public class Scheduled1 {

    @Scheduled(cron = "0 */10 * * * ?")
    public void scheduled1() {
        log.info("scheduled1 run!!");
    }
}
