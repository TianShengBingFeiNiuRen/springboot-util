package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andon
 * 2022/1/4
 * <p>
 * Txt工具类
 */
@Slf4j
public class TxtUtil {

    //行尾分隔符定义
    private final static String NEW_LINE_SEPARATOR = "\n";
    //上传文件的存储位置
    private final static URL PATH = Thread.currentThread().getContextClassLoader().getResource("");

    /**
     * 读TXT文件内容
     */
    public static String readTxtFileToString(File file) {
        StringBuilder result = new StringBuilder();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                result.append(read).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fileReader, bufferedReader);
        }
        return result.toString();
    }

    private static void close(FileReader fileReader, BufferedReader bufferedReader) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileReader != null) {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读TXT文件内容
     */
    public static List<String> readTxtFileToList(File file) {
        List<String> result = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                result.add(read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fileReader, bufferedReader);
        }
        return result;
    }

    /**
     * 创建Txt文件
     *
     * @param fileName File
     * @param values   表体
     */
    public static File makeTempTxt(String fileName, List<String> values) throws IOException {
        // 创建文件
        assert PATH != null;
        File file = File.createTempFile(fileName, ".txt", new File(PATH.getPath()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        // 写入内容
        for (String value : values) {
            bufferedWriter.write(value + NEW_LINE_SEPARATOR);
        }
        bufferedWriter.close();
        return file;
    }

    /**
     * 创建Txt文件
     */
    public static File makeTempTxt(String fileName, String content) throws IOException {
        // 创建文件
        assert PATH != null;
        File file = File.createTempFile(fileName, ".txt", new File(PATH.getPath()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        // 写入内容
        bufferedWriter.write(content);
        bufferedWriter.close();
        return file;
    }

    /**
     * 读取文件返回Map映射
     */
    public static Map<String, String> readTxtReturnMap(File keyFile, File valueFile) {
        Map<String, String> map = new HashMap<>();
        List<String> keyList = readTxtFileToList(keyFile);
        List<String> valueList = readTxtFileToList(valueFile);
        for (int i = 0; i < keyList.size(); i++) {
            if (i == valueList.size()) {
                break;
            }
            map.put(keyList.get(i), valueList.get(i));
        }
        return map;
    }
}
