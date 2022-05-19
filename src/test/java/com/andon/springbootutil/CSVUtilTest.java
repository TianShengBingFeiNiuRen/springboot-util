package com.andon.springbootutil;

import com.andon.springbootutil.util.CSVUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Andon
 * 2022/5/6
 */
@Slf4j
public class CSVUtilTest {

    @Test
    public void test02() {
        String filePath = "D:\\IdeaProjects\\springboot-util\\target\\test-classes\\f.csv";
        int num = 2;
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                try {
                    CSVUtil.recordIterator(filePath, csvRecordIterator -> {
                        int row = 1;
                        while (csvRecordIterator.hasNext()) {
                            CSVRecord csvRecord = csvRecordIterator.next();
                            List<String> list = csvRecord.toList();
                            String join = String.join(",", list);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            log.info("[{}] - row:{} join:{}", Thread.currentThread().getName(), row++, join);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            completableFutures.add(completableFuture);
        }
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        Void join = completableFuture.join();
        log.info("{}", join);
    }

    @Test
    public void test01() throws IOException {
        String filePath = "f.csv";
        File file = CSVUtil.createFile(filePath);
        log.info("file absolutePath:{} path:{} name:{}", file.getAbsolutePath(), file.getPath(), file.getName());

        String[] header = new String[]{"id", "name", "age"};
        List<String[]> headerList = new ArrayList<>();
        headerList.add(header);
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (int n = 0; n < 2; n++) {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                try {
                    CSVUtil.appendContentToFile(file, headerList);
                    for (int i = 0; i < 5; i++) {
                        List<String[]> values = new ArrayList<>();
                        for (int j = 0; j < 3; j++) {
                            int id = Integer.parseInt(i + String.valueOf(j));
                            String name = "i" + i + "_" + "j" + j;
                            String[] value = new String[]{String.valueOf(id), name, String.valueOf(id), Thread.currentThread().getName()};
                            values.add(value);
                        }
                        CSVUtil.appendContentToFile(file, values);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            completableFutures.add(completableFuture);
        }
        log.info("");

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        Void join = completableFuture.join();
    }
}
