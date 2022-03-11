package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    private final static String NEW_LINE_SEPARATOR = "\n";
    //上传文件的存储位置
    private final static URL PATH = Thread.currentThread().getContextClassLoader().getResource("");
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder().setIgnoreEmptyLines(false).setRecordSeparator(NEW_LINE_SEPARATOR).build();

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
     * 读取CSV文件的内容（不含表头）
     *
     * @param filePath 文件存储路径
     */
    public static List<List<String>> readCSV(String filePath) {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            CSVParser parser = CSVFormat.DEFAULT.parse(bufferedReader);
            // 表内容集合，外层 List为行的集合，内层 List为字段集合
            List<List<String>> values = new ArrayList<>();
            int rowIndex = 0;
            // 读取文件每行内容
            List<CSVRecord> records = parser.getRecords();
            int colNum = records.get(0).size(); //通过表头获取列数量
            for (CSVRecord record : records) {
                // 跳过表头
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                // 每行的内容
                List<String> value = new ArrayList<>(colNum + 1);
                for (int i = 0; i < colNum; i++) {
                    value.add(record.get(i));
                }
                values.add(value);
                rowIndex++;
            }
            return values;
        } catch (IOException e) {
            log.error("解析CSV内容失败 error:{} e:{}", e.getMessage(), e);
        } finally {
            //关闭流
            close(bufferedReader, inputStreamReader, fileInputStream);
        }
        return new ArrayList<>();
    }

    /**
     * 读取CSV文件的内容（不含表头）
     *
     * @param filePath 文件存储路径
     */
    public static Map<String, String> readCSVToMap(String filePath, String[] indexArr) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return records(fileInputStream, csvRecords -> {
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
     */
    public static List<String> readCSVToList(String filePath, String[] indexArr) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return readCSVToList(fileInputStream, indexArr);
        }
    }

    public static List<String> readCSVToList(InputStream inputStream, String[] indexArr) throws IOException {
        return records(inputStream, csvRecords -> {
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

    /**
     * 读取CSV数据条目数
     *
     * @param filePath 文件路径
     * @return 数据条目数
     */
    public static long readDataCount(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return records(fileInputStream, csvRecords -> Long.valueOf(csvRecords.size()));
        } catch (IOException e) {
            log.error("解析CSV内容失败 error:{} e:{}", e.getMessage(), e);
        }
        return 0;
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

    /**
     * 下载文件
     *
     * @param response HttpServletResponse
     * @param file     File
     */
    public static boolean downloadFile(HttpServletResponse response, File file, String fileName) {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        OutputStream os = null;
        try {
            // 设置csv文件下载头信息
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName + ".csv");
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            os = response.getOutputStream();
            //MS产本头部需要插入BOM
            //如果不写入这几个字节，会导致用Excel打开时，中文显示乱码
            os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            byte[] buffer = new byte[1024];
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bufferedInputStream.read(buffer);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            boolean delete = file.delete();
        }
        return false;
    }

    private static <R> R records(InputStream inputStream, Function<List<CSVRecord>, R> recordsHandler) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {

            CSVParser parser = CSV_FORMAT.parse(bufferedReader);
            // 读取文件每行内容
            List<CSVRecord> records = parser.getRecords();
            return recordsHandler.apply(records);
        }
    }

    private static void close(BufferedReader bufferedReader, InputStreamReader inputStreamReader, FileInputStream fileInputStream) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
