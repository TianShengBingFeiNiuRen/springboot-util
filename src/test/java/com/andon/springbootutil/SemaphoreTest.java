package com.andon.springbootutil;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Andon
 * 2022/5/20
 */
@Slf4j
public class SemaphoreTest {

    private static int permits = 3;
    private static Semaphore semaphore = new Semaphore(permits);

    /**
     * 信号量 测试
     */
    @Test
    public void semaphore() throws Exception {
        int num = 5;
        log.info("有{}个位置可以做核酸", permits);
        log.info("来了{}个人做核酸", num);

        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            semaphore.acquire();
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                try {
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

    /**
     * 中断分片线程 测试
     */
    @Test
    public void interruptTest() throws ExecutionException, InterruptedException {
        List<Thread> threads = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i + 1;
            Future<?> future = executorService.submit(() -> {
                Thread thread = Thread.currentThread();
                threads.add(thread);
                try {
                    log.info("[{}] - thread start!! finalI={}", thread.getId(), finalI);
                    Thread.sleep(finalI * 1000);
                    threadCheck();
                    if (finalI == 3) {
                        throw new Exception("Exception");
                    }
                    log.info("[{}] - thread end!! finalI={}", thread.getId(), finalI);
                } catch (Exception e) {
                    log.info("[{}] - thread interrupt!!", thread.getId());
                    interrupt(threads);
                }
            });
            futures.add(future);
        }
        for (Future<?> future : futures) {
            future.get();
        }
        log.info("end!!");
    }

    /**
     * 线程检查
     * <p>
     * 在线程中计划中断的地方执行
     */
    public void threadCheck() throws InterruptedException {
        Thread.sleep(10);
    }

    /**
     * 中断线程
     * <p>
     * 会在线程检查代码执行时，抛出异常来中断线程
     */
    public void interrupt(List<Thread> threads) {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
