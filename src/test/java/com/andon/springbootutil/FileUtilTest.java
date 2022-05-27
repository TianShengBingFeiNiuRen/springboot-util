package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author Andon
 * 2022/5/19
 */
@Slf4j
public class FileUtilTest {

    @Test
    public void appendContentToFile2() throws Exception {
        File file = FileUtil.createFile("createFile.csv");
        List<String> header = Collections.singletonList("id,name,value");
        FileUtil.appendContentToFile(file, header);
        int total = 2000_0000;
        int row = 100_0000;
        int shard = total / row;

        Semaphore semaphore = new Semaphore(50);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < shard; i++) {
            semaphore.acquire();
            int finalI = i;
            Future<?> future = executorService.submit(() -> {
                try {
                    List<String> lines = new ArrayList<>(row);
                    for (int i1 = 0; i1 < row; i1++) {
                        lines.add(finalI + "," + Thread.currentThread().getName() + "," + i1 + UUID.randomUUID().toString());
                    }
                    FileUtil.appendContentToFile(file, lines);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            });
            futures.add(future);
        }
        for (Future<?> future : futures) {
            future.get();
        }
    }

    @Test
    public void readFileContentByLine2() throws Exception {
        String filePath = "D:\\apps\\file\\createFile.csv";
        int batchSize = 10_0000;
        List<String> lines = new ArrayList<>(batchSize);
        FileUtil.readFileContentByLine(filePath, (bufferedReader) -> {
            String line;
            int shard = 0;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (Exception e) {
                    break;
                }
                lines.add(line);
                if (lines.size() == batchSize) {
                    log.info("shard:{} - lines.size={}", shard += 1, lines.size());
                    lines.clear();
                }
            }
            if (!lines.isEmpty()) {
                log.info("shard:{} - lines.size={}", shard += 1, lines.size());
                lines.clear();
            }
        });
    }

    @Test
    public void appendContentToFile() throws IOException {
        File file = FileUtil.createFile("createFile.csv");
        List<String> header = Collections.singletonList("id,name");
        FileUtil.appendContentToFile(file, header);
        int row = 8;
        List<CompletableFuture<Void>> futures = new ArrayList<>(row);
        for (int i = 0; i < row; i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    List<String> lines = Collections.singletonList(finalI + "," + Thread.currentThread().getName());
                    FileUtil.appendContentToFile(file, lines);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            futures.add(future);
        }
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        completableFuture.join();
    }

    @Test
    public void readFileContentByLine() throws Exception {
        String filePath = "D:\\apps\\file\\createFile.csv";
        int batchSize = 5;
        List<String> lines = new ArrayList<>(batchSize);
        FileUtil.readFileContentByLine(filePath, (bufferedReader) -> {
            String line;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (Exception e) {
                    break;
                }
                lines.add(line);
                if (lines.size() == batchSize) {
                    log.info("lines.size={} lines:{}", lines.size(), JSONObject.toJSONString(lines));
                    lines.clear();
                }
            }
            if (!lines.isEmpty()) {
                log.info("lines.size={} lines:{}", lines.size(), JSONObject.toJSONString(lines));
                lines.clear();
            }
        });
    }

    @Test
    public void readSchemaWithFirstLine() throws Exception {
        String filePath = "D:\\apps\\file\\createFile.csv";
        String schema = FileUtil.readSchemaWithFirstLine(filePath);
    }

    @Test
    public void readDataCount() throws Exception {
        String filePath = "D:\\apps\\file\\createFile.csv";
        int count = FileUtil.readDataCount(filePath);
    }

    @Test
    public void createFileWithContent() throws IOException {
        int row = 8;
        List<String> lines = new ArrayList<>(row);
        for (int i = 0; i < row; i++) {
            lines.add(i + "," + UUID.randomUUID().toString());
        }
        File fileWithContent = FileUtil.createFileWithContent("createFileWithContent.tmp", lines);
    }

    @Test
    public void copy() throws IOException {
        String file = "D:\\apps\\file\\createFile.csv";
        String copy = FileUtil.copy(file, UUID.randomUUID().toString(), "copy.tmp");
    }

    @Test
    public void createFile() throws IOException {
        FileUtil.createFile(UUID.randomUUID().toString(), "HelloWorld.tmp");
    }
}
