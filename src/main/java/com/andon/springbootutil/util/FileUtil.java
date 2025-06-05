package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Andon
 * 2022/5/16
 */
@Slf4j
public class FileUtil {

    public static Path rootFilePath;

    static {
        try {
            String osName = System.getProperty("os.name");
            log.info("osName:{}", osName);
            String userDir = Paths.get(System.getProperty("user.dir")).toString();
            String directoryName = userDir.substring(userDir.lastIndexOf(File.separator) + 1);
            String fileRootPath; //文件存放目录
            if (osName.toLowerCase().contains("windows")) {
                fileRootPath = "C:\\apps\\file\\" + directoryName; // 指定windows系统下文件目录
            } else {
                fileRootPath = "/data/apps/file/" + directoryName; // 指定linux系统下文件目录
            }
            rootFilePath = Paths.get(fileRootPath);
            if (!Files.exists(rootFilePath)) {
                Files.createDirectories(rootFilePath);
            }
            log.info("rootFilePath:{}", rootFilePath.toAbsolutePath());
        } catch (Exception e) {
            log.error("FileUtil init failure!! error:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取绝对路径 Path
     *
     * @param fileNamePath 文件名称路径
     */
    public static Path getRootFilePath(String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }

    /**
     * 创建文件
     *
     * @param fileNamePath 文件名称路径
     */
    public static File createFile(String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.deleteIfExists(path);
        Files.createFile(path);
        log.info("createFile success!! --文件-> [{}]", path.toAbsolutePath().toString());
        return path.toFile();
    }

    /**
     * 创建文件
     *
     * @param filePath 文件路径
     */
    public static File createAbsolutePathFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!Files.exists(file.toPath().getParent())) {
            Files.createDirectories(file.toPath().getParent());
        }
        Files.deleteIfExists(file.toPath());
        log.info("deleteFile success!! --文件-> [{}]", file.getAbsolutePath());
        Files.createFile(file.toPath());
        log.info("createFile success!! --文件-> [{}]", file.getAbsolutePath());
        return file;
    }

    /**
     * 保存文件
     *
     * @param multipartFile multipartFile
     * @param fileNamePath  文件名称路径
     */
    public static String save(MultipartFile multipartFile, String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.deleteIfExists(path);
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        log.info("save success!! --文件-> [{}]", path.toAbsolutePath());
        return path.toAbsolutePath().toString();
    }

    /**
     * 保存文件
     *
     * @param multipartFile multipartFile
     * @param filePath      文件绝对路径
     */
    public static void saveToAbsolutePath(MultipartFile multipartFile, String filePath) throws IOException {
        File file = new File(filePath);
        if (!Files.exists(file.toPath().getParent())) {
            Files.createDirectories(file.toPath().getParent());
        }
        Files.deleteIfExists(file.toPath());
        Files.copy(multipartFile.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        log.info("saveToAbsolutePath success!! --文件-> [{}]", filePath);
    }

    /**
     * 复制文件
     *
     * @param fromFilePath 源文件绝对路径
     * @param fileNamePath 目标文件名称路径
     */
    public static String copy(String fromFilePath, String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.deleteIfExists(path);
        Files.copy(new File(fromFilePath).toPath(), path, StandardCopyOption.REPLACE_EXISTING);
        log.info("copy success!! --文件-> [{}]", path.toAbsolutePath());
        return path.toAbsolutePath().toString();
    }

    /**
     * 复制文件
     *
     * @param fromFilePath 源文件绝对路径
     * @param toFilePath   目标文件绝对路径
     */
    public static void copyToAbsolutePath(String fromFilePath, String toFilePath) throws IOException {
        File toFile = new File(toFilePath);
        if (!Files.exists(toFile.toPath().getParent())) {
            Files.createDirectories(toFile.toPath().getParent());
        }
        Files.copy(new File(fromFilePath).toPath(), toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        log.info("copyToAbsolutePath success!! --文件-> [{}]", toFilePath);
    }

    /**
     * 移动文件
     *
     * @param fromFilePath 源文件绝对路径
     * @param fileNamePath 目标文件名称路径
     */
    public static String move(String fromFilePath, String... fileNamePath) throws IOException {
        Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), fileNamePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.deleteIfExists(path);
        Files.move(new File(fromFilePath).toPath(), path, StandardCopyOption.REPLACE_EXISTING);
        log.info("move success!! --文件-> [{}]", path.toAbsolutePath());
        return path.toAbsolutePath().toString();
    }

    /**
     * 移动文件
     *
     * @param fromFilePath 源文件绝对路径
     * @param filePath     目标文件名称路径
     */
    public static void moveToAbsolutePath(String fromFilePath, String filePath) throws IOException {
        File file = new File(filePath);
        if (!Files.exists(file.toPath().getParent())) {
            Files.createDirectories(file.toPath().getParent());
        }
        Files.deleteIfExists(file.toPath());
        Files.move(new File(fromFilePath).toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        log.info("moveToAbsolutePath success!! --文件-> [{}]", filePath);
    }

    /**
     * 删除临时文件目录
     *
     * @param tempFilePath 临时文件目录绝对路径
     */
    public static void deleteTempFilePath(Path tempFilePath) {
        if (Files.exists(tempFilePath)) {
            try (Stream<Path> walkStream = Files.walk(tempFilePath)) {
                walkStream.sorted((p1, p2) -> -p1.compareTo(p2))
                        .forEach(path -> {
                            try {
                                if (Files.isDirectory(path)) {
                                    if (Files.isSymbolicLink(path)) {
                                        Files.delete(path);
                                    } else {
                                        Files.delete(path);
                                    }
                                } else {
                                    Files.delete(path);
                                }
                            } catch (Exception e) {
                                log.error("deleteTempFilePath failure!! " + e);
                            }
                        });
            } catch (Exception e) {
                log.error("deleteTempFilePath failure!! " + e);
            }
        }
    }

    /**
     * 根据内容创建文件
     *
     * @param lines    文件内容
     * @param fileName 文件名称
     */
    public static File createFileWithContent(List<String> lines, String... fileName) throws IOException {
        File file = createFile(fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            log.info("createFileWithContent success!! --文件-> [{}]", file.getAbsolutePath());
            return file;
        }
    }

    /**
     * 根据内容创建文件
     *
     * @param lines    文件内容
     * @param filePath 文件名路径
     */
    public static void createAbsolutePathFileWithContent(List<String> lines, String filePath) throws IOException {
        File file = createAbsolutePathFile(filePath);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            log.info("createAbsolutePathFileWithContent success!! --文件-> [{}]", filePath);
        }
    }

    /**
     * 增量写文件
     *
     * @param file  目标文件对象
     * @param lines 文件内容
     */
    public static void appendContentToFile(File file, List<String> lines) throws IOException {
        synchronized (FileUtil.class) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                for (String line : lines) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }
        }
    }

    /**
     * 读大文件
     *
     * @param filePath      文件绝对路径
     * @param contentReader contentReader
     */
    public static void readFileContentByLine(String filePath, Consumer<BufferedReader> contentReader) throws Exception {
        String charset = charset(filePath);
        try (FileInputStream inputStream = new FileInputStream(filePath);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            contentReader.accept(bufferedReader);
        }
    }

    /**
     * 读文件首行
     *
     * @param filePath 文件绝对路径
     */
    public static String readFirstLine(String filePath) throws Exception {
        AtomicReference<String> firstLine = new AtomicReference<>("");
        readFileContentByLine(filePath, bufferedReader -> {
            try {
                firstLine.set(bufferedReader.readLine());
            } catch (Exception e) {
                firstLine.set("");
            }
        });
        log.info("readFirstLine success!! --文件-> [{}] schema={}", filePath, firstLine);
        return firstLine.get();
    }

    /**
     * 读小文件
     *
     * @param filePath 文件绝对路径
     */
    public static String readContent(String filePath) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        readFileContentByLine(filePath, bufferedReader -> {
            String line;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (Exception e) {
                    break;
                }
                stringBuilder.append(line);
            }
        });
        log.info("readFirstLine success!! --文件-> [{}]", filePath);
        return stringBuilder.toString();
    }

    /**
     * 读取文件行数
     *
     * @param filePath 文件绝对路径
     */
    public static int readDataCount(String filePath) throws Exception {
        int linesCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                linesCount++;
            }
        }
        log.info("readDataCount success!! --文件-> [{}] linesCount={}", filePath, linesCount);
        return linesCount;
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
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            os = response.getOutputStream();
            if (fileName.endsWith(".csv") || fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                // MS产本头部需要插入BOM
                // 如果不写入这几个字节，会导致用Excel打开时，中文显示乱码
                os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            }
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
