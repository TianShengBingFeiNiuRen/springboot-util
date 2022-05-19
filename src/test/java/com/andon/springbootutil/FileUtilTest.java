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
import java.util.concurrent.CompletableFuture;

/**
 * @author Andon
 * 2022/5/19
 */
@Slf4j
public class FileUtilTest {

    @Test
    public void appendContentToFile() throws IOException {
        File file = FileUtil.createFile("createFile.csv");
        List<String> header = Collections.singletonList("id,name");
        FileUtil.appendContentToFile(file, header);
        int row = 5;
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
        List<String> lines = new ArrayList<>();
        FileUtil.readFileContentByLine(filePath, (bufferedReader) -> {
            String line;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (Exception e) {
                    break;
                }
                lines.add(line);
            }
        });
        log.info("lines:{}", JSONObject.toJSONString(lines));
    }

    @Test
    public void readDataCount() throws Exception {
        String filePath = "D:\\apps\\file\\createFile.csv";
        int count = FileUtil.readDataCount(filePath);
    }

    @Test
    public void createFileWithContent() throws IOException {
        int row = 5;
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
