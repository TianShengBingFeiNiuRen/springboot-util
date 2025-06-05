package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Andon
 * 2022/11/3
 */
@Slf4j
public class ZipUtil {

    /**
     * 压缩文件
     *
     * @param sourceFilePath 源文件路径
     * @param zipFilePath    压缩文件路径
     */
    public static File compressToZip(String sourceFilePath, String zipFilePath) {
        try {
            File zipFile = new File(zipFilePath);
            if (!Files.exists(zipFile.toPath().getParent())) {
                Files.createDirectories(zipFile.toPath().getParent());
            }
            File sourceFile = new File(sourceFilePath);
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
                writeZip(sourceFile, "", zos);
            }
            log.info("compressToZip success!! --文件-> [{}]", zipFile.getAbsolutePath());
            return zipFile;
        } catch (Exception e) {
            log.error("compressToZip failure!! error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 遍历所有文件，压缩
     *
     * @param file       源文件目录
     * @param parentPath 压缩文件目录
     * @param zos        文件流
     */
    public static void writeZip(File file, String parentPath, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            //目录
            parentPath += file.getName() + File.separator;
            File[] files = file.listFiles();
            assert files != null;
            for (File f : files) {
                writeZip(f, parentPath, zos);
            }
        } else {
            //文件
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                //指定zip文件夹
                //ZipEntry zipEntry = new ZipEntry(parentPath + file.getName());
                //生成的zip不包含该文件夹
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                int len;
                byte[] buffer = new byte[1024 * 10];
                while ((len = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, len);
                    zos.flush();
                }
            }
        }
    }

    /**
     * 解压 ZIP 文件到指定目录
     *
     * @param zipFilePath ZIP 文件路径
     * @param targetDir   目标解压目录
     */
    public static String decompress(String zipFilePath, String targetDir) {
        try {
            // 校验目标目录是否存在，不存在则创建
            Path targetPath = Paths.get(targetDir);
            if (!Files.exists(targetPath)) {
                Files.createDirectories(targetPath);
            }

            // 使用 try-with-resources 自动关闭流
            try (ZipInputStream zis = new ZipInputStream(
                    new FileInputStream(zipFilePath),
                    Charset.forName("GBK") // 指定 ZIP 条目的编码（解决中文乱码）
            )) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    // 1. 校验条目路径安全性（防止 Zip Slip 攻击）
                    String entryName = entry.getName();
                    Path entryPath = targetPath.resolve(entryName).normalize(); // 标准化路径（去除 ../ 等）
                    if (!entryPath.startsWith(targetPath)) {
                        throw new SecurityException("非法 ZIP 条目路径：" + entryName);
                    }

                    // 2. 处理目录或文件
                    if (entry.isDirectory()) {
                        // 创建目录（含父目录）
                        Files.createDirectories(entryPath);
                    } else {
                        // 创建父目录（防止文件路径中的目录不存在）
                        Path parentDir = entryPath.getParent();
                        if (!Files.exists(parentDir)) {
                            Files.createDirectories(parentDir);
                        }

                        // 写入文件内容
                        try (OutputStream os = Files.newOutputStream(entryPath)) {
                            byte[] buffer = new byte[8192];
                            int len;
                            while ((len = zis.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                    }
                    zis.closeEntry(); // 关闭当前条目
                }
            }
            log.info("decompress success!! --文件夹路径-> [{}]", targetDir);
            return targetDir;
        } catch (Exception e) {
            log.error("decompress failure!! error:{}", e.getMessage());
        }
        return null;
    }
}
