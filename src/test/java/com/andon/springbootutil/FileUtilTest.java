package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Andon
 * 2022/5/19
 */
@Slf4j
public class FileUtilTest {

    @Test
    public void delete() throws IOException {
        String parent = "D:\\Temp\\ceshi";
        try (Stream<Path> walk = Files.walk(Paths.get(parent))) {
            walk.filter(path -> path.toFile().isFile())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception ignored) {
        }
    }

    @Test
    public void createCsvFile() throws Exception {
        String suffix = "data";
        int row = 1_0000;
        int confuseRow = 5_000;
        String fileName = String.format("%s_%s_%s", suffix, row, confuseRow);
//        String[] header = new String[]{"id", "x1_" + suffix, "x2_" + suffix, "x3_" + suffix, "x4_" + suffix, "x5_" + suffix, "x6_" + suffix};
        String[] header = new String[]{"id", "x1_" + suffix, "x2_" + suffix, "x3_" + suffix};
//        String[] header = new String[]{"id"};
        File csvFile = createCsvFile(fileName, header, row, confuseRow);
        log.info("csvFile:{}", csvFile.getAbsolutePath());
    }

    /**
     * @param fileName 文件名
     * @param header   表头
     * @param row      数据行数
     */
    private File createCsvFile(String fileName, String[] header, int row, int confuseRow) throws Exception {
        File file = FileUtil.createFile(fileName + ".csv");
        FileUtil.appendContentToFile(file, Collections.singletonList(String.join(",", header)));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String date = simpleDateFormat.format(System.currentTimeMillis());
        int batchSize = 5000;
        List<String> lines = new ArrayList<>(batchSize);
        for (int i = 0; i < row; i++) {
            List<String> list = new ArrayList<>(header.length);
            for (int column = 0; column < header.length; column++) {
                if (column == 0) {
                    if (confuseRow > 0 && i >= confuseRow) {
                        list.add((i + 1) + "_" + fileName);
                    } else {
                        list.add(date + "_" + (i + 1));
                    }
                } else {
                    list.add(header[column] + "_" + (i + 1));
                }
            }
            lines.add(String.join(",", list));
            if (lines.size() == batchSize) {
                FileUtil.appendContentToFile(file, lines);
                lines.clear();
            }
        }
        if (!lines.isEmpty()) {
            FileUtil.appendContentToFile(file, lines);
            lines.clear();
        }
        return file;
    }

    @Test
    public void readHashTest() throws Exception {
        String datasetId55 = "9c5836fa-1f2f-4589-b9ea-d0d518d0acc9";
        String datasetId79 = "60ab6ee9-270c-4d29-89a8-0d369e44e946";
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
