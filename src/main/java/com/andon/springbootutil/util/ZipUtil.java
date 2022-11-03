package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Andon
 * 2022/11/3
 */
@Slf4j
public class ZipUtil {

    private static Path rootFilePath;

    static {
        try {
            rootFilePath = Paths.get(System.getProperty("user.dir"));
            if (!Files.exists(rootFilePath)) {
                Files.createDirectories(rootFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件
     *
     * @param sourceFilePath 源文件路径
     * @param zipFilename    压缩文件名
     */
    public static File compressToZip(String sourceFilePath, String zipFilename) {
        try {
            Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), zipFilename);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            File zipFile = path.toFile();
            File sourceFile = new File(sourceFilePath);
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
                writeZip(sourceFile, "", zos);
            }
            return zipFile;
        } catch (IOException e) {
            log.error("zip failure!! error:{}", e.getMessage());
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
}
