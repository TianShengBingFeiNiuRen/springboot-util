package com.andon.springbootutil.constant;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Andon
 * 2025/5/27
 */
public class Constants {

    public static String DATA_PATH;

    static {
        try {
            String osName = System.getProperty("os.name");
            String userDir = Paths.get(System.getProperty("user.dir")).toString();
            String directoryName = userDir.substring(userDir.lastIndexOf(File.separator) + 1);
            String fileRootPath; //文件存放目录
            if (osName.toLowerCase().contains("windows")) {
                fileRootPath = "C:\\apps\\file\\" + directoryName; // 指定windows系统下文件目录
            } else {
                fileRootPath = "/data/apps/file/" + directoryName; // 指定linux系统下文件目录
            }
            Path dataPath = Paths.get(fileRootPath);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
            DATA_PATH = dataPath.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 升级包相对路径
     */
    public static final String PACKAGE_PATH = "PACKAGE";

    /**
     * 升级包信息
     */
    public static final String PACKAGE_INFO = "package_info.json";

    /**
     * 升级任务
     */
    public static final String UPGRADE_TASK = "upgrade_task.json";

    /**
     * 当前版本信息
     */
    public static final String VERSION_INFO = "version_info.json";

    /**
     * 升级包工作目录
     */
    public static final String UPGRADE_PACKAGE = "upgrade-package";

    /**
     * 升级脚本名称
     */
    public static final String UPGRADE_SH = "upgrade.sh";
}
