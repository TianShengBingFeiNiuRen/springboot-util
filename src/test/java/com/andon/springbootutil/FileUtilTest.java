package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Andon
 * 2022/5/19
 */
@Slf4j
public class FileUtilTest {

    @Test
    public void readHashTest() throws Exception {
        String datasetId55 = "d8259e48-1691-4cb2-9ac5-516e8b02d176";
        String datasetId79 = "c3c988c5-acf5-45c6-8229-69cfe180291b";
        Map<String, Integer> hashNumber55 = readHash(datasetId55);
        Map<String, Integer> hashNumber79 = readHash(datasetId79);

        Set<String> hashRepeat55 = hashRepeat(hashNumber55, hashNumber79);
        Set<String> hashRepeat79 = hashRepeat(hashNumber79, hashNumber55);
        log.info("hashRepeat55.size:{}", hashRepeat55.size());
        log.info("hashRepeat79.size:{}", hashRepeat79.size());
    }

    private Set<String> hashRepeat(Map<String, Integer> hashNumber1, Map<String, Integer> hashNumber2) {
        return hashNumber1.keySet().stream().filter(key -> !ObjectUtils.isEmpty(hashNumber2.get(key)))
                .collect(Collectors.toSet());
    }

    private Map<String, Integer> readHash(String datasetId) throws Exception {
        Map<String, Integer> hashNumber = new HashMap<>();

        String filePath = "F:\\anheng\\Mpc\\hash_" + datasetId + ".data";
        FileUtil.readFileContentByLine(filePath, (bufferedReader) -> {
            String line;
            int shard = 0;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (Exception e) {
                    break;
                }
                hashNumber.put(line, hashNumber.get(line) == null ? 1 : hashNumber.get(line) + 1);
            }
        });

        Map<String, Integer> hashRepeat = hashNumber.entrySet().stream().filter(entry -> entry.getValue() > 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        log.info("{} - hashNumber.size:{}", datasetId, hashNumber.size());
        log.info("{} - hashRepeat:{}", datasetId, JSONObject.toJSONString(hashRepeat));
        return hashNumber;
    }

    @Test
    public void appendContentToFile2() throws Exception {
        File file = FileUtil.createFile("createFile.csv");
        List<String> header = Collections.singletonList("id,name,value");
        FileUtil.appendContentToFile(file, header);
        int total = 50_0000;
        int row = 10000;
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
        String firstLine = FileUtil.readFirstLine(filePath);
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
        File fileWithContent = FileUtil.createFileWithContent(lines, "createFileWithContent.tmp");
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
