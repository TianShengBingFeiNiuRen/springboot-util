package com.andon.springbootutil;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

/**
 * @author Andon
 * 2022/5/20
 */
@Slf4j
public class SemaphoreTest {

    private static int permits = 3;
    private static Semaphore semaphore = new Semaphore(permits);

    @Test
    public void semaphore() {
        int num = 5;
        log.info("有{}个位置可以做核酸", permits);
        log.info("来了{}个人做核酸", num);

        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                try {
                    semaphore.acquire();
                    long stop = (long) (Math.random() * 10 + 1);
                    log.info("{}开始做核酸，耗时{}秒", Thread.currentThread().getName(), stop);
                    Thread.sleep(stop * 1000);
                } catch (Exception ignored) {
                } finally {
                    log.info("{}做完核酸准备离开", Thread.currentThread().getName());
                    semaphore.release();
                    log.info("{}已经离开了!!", Thread.currentThread().getName());
                }
            });
            completableFutures.add(completableFuture);
        }
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        completableFuture.join();
    }
}
