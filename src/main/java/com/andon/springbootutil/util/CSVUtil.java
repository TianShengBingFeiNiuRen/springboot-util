package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Andon
 * 2021/12/21
 * <p>
 * csv文件上传下载
 */
@Slf4j
public class CSVUtil {

    //行尾分隔符定义
    private static final String NEW_LINE_SEPARATOR = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
    //上传文件的存储位置
    private final static URL PATH = Thread.currentThread().getContextClassLoader().getResource("");
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder().setIgnoreEmptyLines(false).setRecordSeparator(NEW_LINE_SEPARATOR).setQuote(null).build();

    /**
     * 上传文件
     *
     * @param multipartFile MultipartFile
     */
    public static File uploadFile(MultipartFile multipartFile) {
        assert PATH != null;
        // 获取上传路径
        String path = PATH.getPath() + UUID.randomUUID().toString().replaceAll("-", "") + File.separator + multipartFile.getOriginalFilename();
        try {
            // 通过将给定的路径名字符串转换为抽象路径名来创建新的 File实例
            File file = new File(path);
            // 此抽象路径名表示的文件或目录是否存在
            if (!file.getParentFile().exists()) {
                // 创建由此抽象路径名命名的目录，包括任何必需但不存在的父目录
                boolean mkdirs = file.getParentFile().mkdirs();
            }
            // 转换为一般file文件
            multipartFile.transferTo(file);
            log.info("上传文件成功，文件名===>{}, 路径===>{}", multipartFile.getOriginalFilename(), file.getPath());
            return file;
        } catch (IOException e) {
            log.error("上传文件失败 error:{} e:{}" + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 读取CSV文件的内容
     *
     * @param filePath 文件路径
     * @param indexArr 参与计算的列的组合角标
     * @return 表内容集合，key是组合ID，value是整行数据
     */
    public static Map<String, String> readCSVToMap(String filePath, String[] indexArr) throws IOException {
        String charset = charset(filePath);
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return records(fileInputStream, charset, csvRecords -> {
                Map<String, String> map = new HashMap<>();
                //通过首行获取列数量
                int colNum = csvRecords.get(0).size();
                for (CSVRecord record : csvRecords) {
                    // 每行的内容
                    List<String> value = new ArrayList<>(colNum);
                    for (int i = 0; i < colNum; i++) {
                        value.add(record.get(i));
                    }
                    // 每行ID
                    List<String> key = new ArrayList<>(indexArr.length);
                    for (String index : indexArr) {
                        key.add(record.get(Integer.parseInt(index)));
                    }
                    String id = String.join(",", key);
                    if (!map.containsKey(id)) {
                        map.put(id, String.join(",", value));
                    }
                }
                return map;
            });
        }
    }

    /**
     * 读取CSV文件的内容
     *
     * @param filePath 文件路径
     * @param indexArr 参与计算的列的组合角标
     * @return 表内容集合，value是参与计算的列的数据
     */
    public static List<String> readCSVToList(String filePath, String[] indexArr) throws IOException {
        String charset = charset(filePath);
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return records(fileInputStream, charset, csvRecords -> {
                List<String> values = new ArrayList<>();
                for (CSVRecord record : csvRecords) {
                    // 每行的内容
                    List<String> value = new ArrayList<>();
                    if (ObjectUtils.isEmpty(indexArr)) {
                        for (String item : record) {
                            value.add(item.trim());
                        }
                    } else {
                        value = new ArrayList<>(indexArr.length);
                        for (String index : indexArr) {
                            value.add(record.get(Integer.parseInt(index)));
                        }
                    }
                    values.add(String.join(",", value));
                }
                return values;
            });
        }
    }

    /**
     * 读取CSV数据条目数
     *
     * @param filePath 文件路径
     * @return 数据条目数
     */
    public static long readDataCount(String filePath) {
        String charset = charset(filePath);
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return records(fileInputStream, charset, csvRecords -> Long.valueOf(csvRecords.size()));
        } catch (IOException e) {
            log.error("解析CSV内容失败 error:{} e:{}", e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 创建文件
     */
    public static File createFile(String... filePath) throws IOException {
        assert PATH != null;
        Path path = Paths.get(PATH.getPath().substring(1), filePath);
        if (!path.getParent().toFile().exists()) {
            Files.createDirectory(path.getParent());
        }
        Files.deleteIfExists(path);
        Files.createFile(path);
        return path.toFile();
    }

    public static void recordIterator(String filePath, Consumer<Iterator<CSVRecord>> recordIteratorHandler) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            recordIterator(fileInputStream, charset(filePath), recordIteratorHandler);
        }
    }

    public static void recordIterator(InputStream inputStream, String charset, Consumer<Iterator<CSVRecord>> recordIteratorHandler) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            CSVParser parser = CSV_FORMAT.parse(bufferedReader);
            recordIteratorHandler.accept(parser.iterator());
        }
    }

    /**
     * 增量写文件
     */
    public static void appendContentToFile(File file, List<String[]> values) throws IOException {
        synchronized (CSVUtil.class) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                CSVPrinter printer = new CSVPrinter(bufferedWriter, CSV_FORMAT);
                for (String[] value : values) {
                    printer.printRecord(Arrays.asList(value));
                }
                printer.close();
            }
        }
    }

    /**
     * 创建CSV文件
     *
     * @param fileName File
     * @param head     表头
     * @param values   表体
     */
    public static File makeTempCSV(String fileName, String[] head, List<String[]> values) throws IOException {
        // 创建文件
        assert PATH != null;
        File file = File.createTempFile(fileName, ".csv", new File(PATH.getPath()));
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            CSVPrinter printer = new CSVPrinter(bufferedWriter, CSV_FORMAT);
            // 写入表头
            printer.printRecord(Arrays.asList(head));
            // 写入内容
            for (String[] value : values) {
                printer.printRecord(Arrays.asList(value));
            }
            printer.close();
        }
        return file;
    }

    private static <R> R records(InputStream inputStream, String charset, Function<List<CSVRecord>, R> recordsHandler) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            CSVParser parser = CSV_FORMAT.parse(bufferedReader);
            // 读取文件每行内容
            List<CSVRecord> records = parser.getRecords();
            return recordsHandler.apply(records);
        }
    }

    /**
     * 判断文件字符编码
     */
    public static String charset(String path) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset; // 文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; // 文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; // 文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; // 文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 > read || read > 0xBF) {
                            break; // 双字节 (0xC0 - 0xDF),(0x80 - 0xBF),也可能在GB编码内
                        }
                    } else if (0xE0 <= read) { // 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                            }
                        }
                        break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("--文件-> [{}] 采用的字符集为: [{}]", path, charset);
        return charset;
    }
}
