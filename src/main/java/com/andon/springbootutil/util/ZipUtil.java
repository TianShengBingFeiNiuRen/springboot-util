package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Andon
 * 2022/9/5
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
     * 压缩
     *
     * @param inputFilePath 要压缩的文件目录
     * @param zipFileName   压缩后的文件名称
     * @return 压缩后的文件对象
     */
    public static File zipFilePath(String inputFilePath, String zipFileName) {
        try {
            Path path = Paths.get(rootFilePath.toAbsolutePath().toString(), zipFileName);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path.toFile()));
            File inputFile = new File(inputFilePath);
            compress(inputFile, zipOutputStream, "");
            // 递归压缩文件夹
            zipOutputStream.finish();
            zipOutputStream.close();
            return path.toFile();
        } catch (Exception e) {
            log.error("zip failure!! error:{}", e.getMessage());
            return null;
        }
    }

    private static void compress(File inputFile, ZipOutputStream zipOutputStream, String name) throws IOException {
        byte[] buf = new byte[1024];
        if (inputFile.isFile()) {
            // 压缩单个文件，压缩后文件名为当前文件名
            zipOutputStream.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            while ((len = fileInputStream.read(buf)) != -1) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
            fileInputStream.close();
        } else {
            File[] listFiles = inputFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 空文件夹的处理(创建一个空ZipEntry)
                zipOutputStream.putNextEntry(new ZipEntry(name + "/"));
                zipOutputStream.closeEntry();
            } else {
                // 递归压缩文件夹下的文件
                for (File file : listFiles) {
                    compress(file, zipOutputStream, name + "/" + file.getName());
                }
            }
        }
    }
}
