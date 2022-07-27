package com.andon.springbootutil;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Andon
 * 2022/7/27
 */
@Slf4j
public class CountDownLatchTest {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Test
    public void countDownLatchTest() throws InterruptedException {
        int num = 5;
        CountDownLatch latch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            executorService.submit(() -> {
                long sleep = (long) (Math.random() * 10 + 1);
                log.info("sleep:{} start!!", sleep);
                try {
                    TimeUnit.SECONDS.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                log.info("sleep:{} end!!", sleep);
            });
        }
        log.info("latch await start!!");
        latch.await();
        log.info("latch await end!!");
    }
}
