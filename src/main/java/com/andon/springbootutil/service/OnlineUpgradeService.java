package com.andon.springbootutil.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.andon.springbootutil.constant.Constants;
import com.andon.springbootutil.constant.UpgradeStatus;
import com.andon.springbootutil.constant.UploadMethod;
import com.andon.springbootutil.dto.PackageInfo;
import com.andon.springbootutil.dto.UpgradeTask;
import com.andon.springbootutil.dto.VersionInfo;
import com.andon.springbootutil.request.PackageUploadReq;
import com.andon.springbootutil.request.UpgradeReq;
import com.andon.springbootutil.util.DigestUtil;
import com.andon.springbootutil.util.FileUtil;
import com.andon.springbootutil.util.TimeUtil;
import com.andon.springbootutil.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Andon
 * 2025/5/27
 */
@Slf4j
@Service
public class OnlineUpgradeService {

    public static Map<String, String> TEMP_FILE_PATH = new ConcurrentHashMap<>();

    public static List<PackageInfo> PACKAGE_INFO_LIST = new CopyOnWriteArrayList<>();
    public static Map<String, PackageInfo> PACKAGE_INFO_MAP = new ConcurrentHashMap<>();

    public static List<UpgradeTask> UPGRADE_TASK_LIST = new CopyOnWriteArrayList<>();

    public static VersionInfo VERSION_INFO = VersionInfo.builder().beforeVersion("-").afterVersion("init").build();

    private static boolean readLog = false;

    @PostConstruct
    public void init() {
        try {
            VERSION_INFO = readVersionInfo();

            List<PackageInfo> packageInfoList = readPackageInfoList();
            PACKAGE_INFO_LIST = new CopyOnWriteArrayList<>(packageInfoList);

            Map<String, PackageInfo> packageInfoMap = new HashMap<>(packageInfoList.size());
            for (PackageInfo packageInfo : packageInfoList) {
                packageInfoMap.put(packageInfo.getId(), packageInfo);
                PACKAGE_INFO_MAP = new ConcurrentHashMap<>(packageInfoMap);
            }

            List<UpgradeTask> upgradeTaskList = readUpgradeTaskList();
            UPGRADE_TASK_LIST = new CopyOnWriteArrayList<>(upgradeTaskList);

            Path tempFilePath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, ".tmp");
            FileUtil.deleteTempFilePath(tempFilePath);
        } catch (Exception e) {
            log.error("init failure!! " + e);
        }
    }

    /**
     * 包管理-上传
     */
    public String packageUpload(MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();
        Assert.isTrue(originalFilename != null, "升级包损坏，请重新上传");
        int lastDotIndex = originalFilename.lastIndexOf('.');
        Assert.isTrue(lastDotIndex != -1, "升级包无格式，请重新上传");
        String fileSuffix = originalFilename.substring(lastDotIndex).toLowerCase();
        Assert.isTrue(".zip".equals(fileSuffix), "升级包非zip格式，请重新上传");

        String tempFileId = UUID.randomUUID().toString();
        Path tempFilePath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, ".tmp", tempFileId, originalFilename);
        FileUtil.saveToAbsolutePath(multipartFile, tempFilePath.toString());
        TEMP_FILE_PATH.put(tempFileId, tempFilePath.toString());
        return tempFileId;
    }

    /**
     * 包管理-创建
     */
    public String packageCreate(PackageUploadReq packageUploadReq) throws Exception {
        for (PackageInfo packageInfo : PACKAGE_INFO_LIST) {
            Assert.isTrue(!packageInfo.getVersion().equals(packageUploadReq.getVersion()), "版本号已存在,请重新填写");
        }

        long timestamp = System.currentTimeMillis();
        String id = String.format("%s_%s", packageUploadReq.getVersion(), timestamp);
        Path filePath = null;

        if (UploadMethod.WEB.equals(packageUploadReq.getUploadMethod())) {
            Assert.isTrue(!ObjectUtils.isEmpty(packageUploadReq.getFileId()), "升级包文件ID不能为空");
            String tempFilePath = TEMP_FILE_PATH.get(packageUploadReq.getFileId());
            Assert.isTrue(!ObjectUtils.isEmpty(tempFilePath), "临时文件已被清理，请重新上传升级包");
            File tempFile = new File(tempFilePath);
            Assert.isTrue(Files.exists(tempFile.toPath()), "临时文件已被清理，请重新上传升级包");

            filePath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, id, tempFile.getName());
            FileUtil.moveToAbsolutePath(tempFilePath, filePath.toString());
            TEMP_FILE_PATH.remove(packageUploadReq.getFileId());
        } else if (UploadMethod.SERVER.equals(packageUploadReq.getUploadMethod())) {
            String serverFilePath = packageUploadReq.getServerFilePath();
            Assert.isTrue(!ObjectUtils.isEmpty(serverFilePath), "服务器文件路径不能为空");
            File serverFile = new File(serverFilePath);
            Assert.isTrue(Files.exists(serverFile.toPath()), "服务文件已被清理，请重新上传升级包");

            filePath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, id, serverFile.getName());
            FileUtil.copyToAbsolutePath(packageUploadReq.getServerFilePath(), filePath.toString());
        }

        assert filePath != null;
        String md5 = DigestUtil.getHash(DigestUtil.MD5, filePath.toFile());
        PackageInfo packageInfo = PackageInfo.builder().id(id).name(packageUploadReq.getName()).version(packageUploadReq.getVersion())
                .desc(packageUploadReq.getDesc()).md5(md5).filePath(filePath.toString()).uploadTime(TimeUtil.FORMAT.get().format(timestamp)).build();
        savePackageInfo(packageInfo);
        return id;
    }

    /**
     * 节点升级
     */
    public String upgrade(UpgradeReq upgradeReq) throws Exception {
        String packageId = upgradeReq.getPackageId();
        PackageInfo packageInfo = PACKAGE_INFO_MAP.get(packageId);
        Assert.isTrue(!ObjectUtils.isEmpty(packageInfo) && !ObjectUtils.isEmpty(packageInfo.getFilePath()), "选择的升级包已被清理，请重新选择升级包");
        File pacakgeFile = new File(packageInfo.getFilePath());
        Assert.isTrue(Files.exists(pacakgeFile.toPath()), "选择的升级包已被清理，请重新选择升级包");

        if (!Boolean.TRUE.equals(upgradeReq.getImmediately())) {
            Assert.isTrue(TimeUtil.checkFormat(upgradeReq.getUpgradeTime(), TimeUtil.FORMAT.get()), "请输入正确的升级时间：yyyy-MM-dd HH:mm:ss");
        }

        if (!ObjectUtils.isEmpty(UPGRADE_TASK_LIST)) {
            int lastIndex = UPGRADE_TASK_LIST.size() - 1;
            UpgradeTask upgradeTaskLast = UPGRADE_TASK_LIST.get(lastIndex);
            if (UpgradeStatus.WAIT.equals(upgradeTaskLast.getStatus()) || UpgradeStatus.UPGRADING.equals(upgradeTaskLast.getStatus())) {
                upgradeTaskLast.setStatus(UpgradeStatus.INVALID);
            }
        }

        long timestamp = System.currentTimeMillis();
        String id = String.format("%s_%s", packageInfo.getVersion(), timestamp);
        UpgradeTask upgradeTask = UpgradeTask.builder().id(id).beforePackageId(VERSION_INFO.getAfterPackageId()).beforeVersion(VERSION_INFO.getAfterVersion())
                .afterPackageId(packageInfo.getId()).afterVersion(packageInfo.getVersion()).md5(packageInfo.getMd5()).status(UpgradeStatus.WAIT)
                .upgradeTime(!Boolean.TRUE.equals(upgradeReq.getImmediately()) ? upgradeReq.getUpgradeTime() : null).build();
        if (!Boolean.TRUE.equals(upgradeReq.getImmediately())) {
            upgradeTask.setUpgradeTime(upgradeReq.getUpgradeTime());
        }
        saveUpgradeTask(upgradeTask);
        return id;
    }

    /**
     * 节点升级
     */
    @Scheduled(initialDelay = 30, fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void upgrade() {
        if (!readLog) {
            VERSION_INFO = readVersionInfo();
            List<UpgradeTask> upgradeTaskList = readUpgradeTaskList();
            UPGRADE_TASK_LIST = new CopyOnWriteArrayList<>(upgradeTaskList);
            readLog = true;
        }
        if (ObjectUtils.isEmpty(UPGRADE_TASK_LIST)) {
            return;
        }
        int lastIndex = UPGRADE_TASK_LIST.size() - 1;
        UpgradeTask upgradeTask = UPGRADE_TASK_LIST.get(lastIndex);
        if (!UpgradeStatus.WAIT.equals(upgradeTask.getStatus())) {
            return;
        }
        if (!ObjectUtils.isEmpty(upgradeTask.getUpgradeTime())) {
            String upgradeTime = upgradeTask.getUpgradeTime();
            try {
                Date upgradeDate = TimeUtil.FORMAT.get().parse(upgradeTime);
                if (upgradeDate.after(new Date())) {
                    return;
                }
            } catch (Exception e) {
                log.warn("ID:{} afterVersion:{} 非法升级时间 upgradeTime:{}", upgradeTask.getId(), upgradeTask.getAfterVersion(), upgradeTask);
            }
        }
        PackageInfo packageInfo = PACKAGE_INFO_MAP.get(upgradeTask.getAfterPackageId());

        long timestamp = System.currentTimeMillis();
        upgradeTask.setStatus(UpgradeStatus.UPGRADING);
        upgradeTask.setUpgradeTime(ObjectUtils.isEmpty(upgradeTask.getUpgradeTime()) ? TimeUtil.FORMAT.get().format(timestamp) : upgradeTask.getUpgradeTime());
        StringBuilder logBuilder = new StringBuilder(!ObjectUtils.isEmpty(upgradeTask.getLog()) ? upgradeTask.getLog() : "");

        try {
            String userDir = System.getProperty("user.dir");
            // TODO 1.检查升级包是否存在
            log.info("[{}] 1. 检查升级包 [{}] 是否存在", upgradeTask.getId(), packageInfo.getVersion());
            upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                    .append("1. 检查 [").append(packageInfo.getVersion()).append("] 升级包是否存在").append(System.lineSeparator()).toString());

            File pacakgeFile = new File(packageInfo.getFilePath());
            if (!Files.exists(pacakgeFile.toPath())) {
                log.warn("[{}] 升级包 [{}] 不存在，无法升级", upgradeTask.getId(), packageInfo.getVersion());
                upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" WARN ")
                        .append(" [").append(packageInfo.getVersion()).append("] 升级包不存在，无法升级").append(System.lineSeparator()).toString());

                upgradeTask.setStatus(UpgradeStatus.FAILURE);
            } else {
                // TODO 2.拉取升级包到升级包工作目录
                log.info("[{}] 2. 拉取升级包 [{}] 到升级包工作目录", upgradeTask.getId(), packageInfo.getVersion());
                upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                        .append("2. 拉取升级包 [").append(packageInfo.getVersion()).append("] 到升级包工作目录").append(System.lineSeparator()).toString());

                Path upgradePackageParentPath = Paths.get(Constants.DATA_PATH, Constants.UPGRADE_PACKAGE);
                FileUtil.deleteTempFilePath(upgradePackageParentPath);

                Path upgradePackagePath = Paths.get(upgradePackageParentPath.toString(), pacakgeFile.getName());
                FileUtil.copyToAbsolutePath(pacakgeFile.getAbsolutePath(), upgradePackagePath.toString());
                upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                        .append("->>> pull success!! ---> [").append(upgradePackagePath).append("]").append(System.lineSeparator()).toString());
                // TODO 3.解压升级包
                log.info("[{}] 3. 解压升级包 [{}]", upgradeTask.getId(), packageInfo.getVersion());
                upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                        .append("3. 解压升级包 [").append(packageInfo.getVersion()).append("]").append(System.lineSeparator()).toString());

                ZipUtil.decompress(upgradePackagePath.toString(), upgradePackageParentPath.toString());
                upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                        .append("->>> decompress success!! --文件夹路径-> [").append(upgradePackageParentPath).append("]").append(System.lineSeparator()).toString());

                // TODO 4.升级前后包版本等信息写入版本文件
                log.info("[{}] 4. 升级前后包版本等信息写入版本文件 [{}] -> [{}]", upgradeTask.getId(), VERSION_INFO.getAfterVersion(), packageInfo.getVersion());
                Path upgradePackageVersionPath = Paths.get(upgradePackageParentPath.toString(), Constants.VERSION_INFO);

                upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                        .append("4. 升级前后包版本等信息写入版本文件 [").append(VERSION_INFO.getAfterVersion())
                        .append("] -> [").append(packageInfo.getVersion()).append("]").append(" --文件-> [").append(upgradePackageVersionPath).append("]").append(System.lineSeparator()).toString());
                saveUpgradeTask(upgradeTask);

                VersionInfo versionInfo = VersionInfo.builder().id(upgradeTask.getId()).beforePackageId(VERSION_INFO.getAfterPackageId())
                        .beforeVersion(VERSION_INFO.getAfterVersion()).beforePath(userDir)
                        .afterPackageId(packageInfo.getId()).afterVersion(packageInfo.getVersion())
                        .md5(packageInfo.getMd5()).upgradeTime(upgradeTask.getUpgradeTime()).status(upgradeTask.getStatus()).log(upgradeTask.getLog()).build();
                FileUtil.createAbsolutePathFileWithContent(Collections.singletonList(JSONObject.toJSONString(versionInfo)), upgradePackageVersionPath.toString());

                // TODO 5.执行升级脚本
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    log.warn("[{}] windows系统跳过", upgradeTask.getId());
                    throw new IllegalArgumentException("windows系统暂不支持在线升级");
                } else {
                    Path upgradeScriptPath = Paths.get(upgradePackageParentPath.toString(), Constants.UPGRADE_SH);
                    if (!Files.exists(upgradeScriptPath)) {
                        log.warn("[{}] 5.1 升级包中未检测到升级脚本 [{}]，尝试使用已部署的升级脚本", upgradeTask.getId(), upgradeScriptPath);
                        upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" WARN ")
                                .append("5.1 升级包中未检测到升级脚本 [").append(upgradeScriptPath).append("]，尝试使用已部署的升级脚本").append(System.lineSeparator()).toString());
                        Path deployUpgradeScriptPath = Paths.get(userDir, Constants.UPGRADE_SH);
                        Assert.isTrue(Files.exists(deployUpgradeScriptPath), String.format("升级脚本[%s]不存在，请在升级包中加入升级脚本", deployUpgradeScriptPath));

                        FileUtil.copyToAbsolutePath(deployUpgradeScriptPath.toString(), upgradeScriptPath.toString());
                        upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                                .append("->>> pull success!! ---> [").append(deployUpgradeScriptPath).append("] ---> [").append(upgradeScriptPath).append("]").append(System.lineSeparator()).toString());
                    }else {
                        log.warn("[{}] 5.1 升级包中未检测到升级脚本 [{}]，将使用升级包中的升级脚本", upgradeTask.getId(), upgradeScriptPath);
                        upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                                .append("5.1 升级包中未检测到升级脚本 [").append(upgradeScriptPath).append("]，将使用升级包中的升级脚本").append(System.lineSeparator()).toString());
                    }

                    log.info("[{}] 5.2 赋权升级脚本  -> [chmod +x {}]", upgradeTask.getId(), upgradeScriptPath);
                    upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                            .append("5.2 赋权升级脚本 -> [chmod +x ").append(upgradeScriptPath).append("]").append(System.lineSeparator()).toString());

                    ProcessBuilder processBuilder = new ProcessBuilder("chmod", "+x", upgradeScriptPath.toString());
                    processBuilder.start();
                    processBuilder = new ProcessBuilder("sed", "-i", "'s/\\r$//'", upgradeScriptPath.toString());
                    processBuilder.start();

                    log.info("[{}] 5.3 执行升级脚本 -> [/bin/sh {}]", upgradeTask.getId(), upgradeScriptPath);
                    upgradeTask.setLog(logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" INFO ")
                            .append("5.3 执行升级脚本 -> [/bin/sh ").append(upgradeScriptPath).append("]").toString());
                    processBuilder = new ProcessBuilder("/bin/sh", upgradeScriptPath.toString());
                    versionInfo.setLog(upgradeTask.getLog());
                    FileUtil.createAbsolutePathFileWithContent(Collections.singletonList(JSONObject.toJSONString(versionInfo)), upgradePackageVersionPath.toString());
                    saveUpgradeTask(upgradeTask);
                    processBuilder.start();

                    boolean readLog = true;
                    long costTime = 2 * 60 * 60 * 1000L;
                    while (readLog) {
                        TimeUnit.SECONDS.sleep(10);
                        try {
                            String versionInfoJson = FileUtil.readContent(upgradePackageVersionPath.toString());
                            versionInfo = JSONObject.parseObject(versionInfoJson, new TypeReference<VersionInfo>() {
                            }.getType());
                        } catch (Exception e) {
                            // 日志文件更新中会读取异常
                            continue;
                        }

                        for (UpgradeTask upgradeHistory : UPGRADE_TASK_LIST) {
                            if (upgradeHistory.getId().equals(upgradeTask.getId())) {
                                if (UpgradeStatus.INVALID.equals(upgradeHistory.getStatus()) || UpgradeStatus.FAILURE.equals(upgradeHistory.getStatus()) || UpgradeStatus.SUCCESS.equals(upgradeHistory.getStatus())) {
                                    readLog = false;
                                } else {
                                    upgradeHistory.setStatus(versionInfo.getStatus());
                                    upgradeHistory.setLog(versionInfo.getLog());
                                    if (UpgradeStatus.FAILURE.equals(versionInfo.getStatus()) || UpgradeStatus.SUCCESS.equals(versionInfo.getStatus())) {
                                        readLog = false;
                                    }
                                    if (UpgradeStatus.SUCCESS.equals(versionInfo.getStatus())) {
                                        VERSION_INFO = versionInfo;
                                    }
                                    if (System.currentTimeMillis() - timestamp > costTime) {
                                        log.warn("[{}] 升级耗时超过2小时，请联系运维管理员手动升级", upgradeTask.getId());
                                        StringBuilder upgradeHistoryLogBuilder = new StringBuilder(!ObjectUtils.isEmpty(upgradeHistory.getLog()) ? upgradeHistory.getLog() : "");
                                        upgradeHistory.setLog(upgradeHistoryLogBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" WARN 升级耗时超过2小时，请联系运维管理员手动升级").toString());
                                        readLog = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("[{}] 发生异常 error:" + e, upgradeTask.getId());
            StringBuilder logAppend = logBuilder.append(TimeUtil.FORMAT.get().format(System.currentTimeMillis())).append(" ERROR ")
                    .append("发生异常 error:").append(System.lineSeparator())
                    .append(e.getClass()).append(System.lineSeparator()).append(e.getLocalizedMessage()).append(System.lineSeparator());
            StackTraceElement[] arr = e.getStackTrace();
            for (StackTraceElement stackTraceElement : arr) {
                logAppend.append(stackTraceElement.toString()).append(System.lineSeparator());
            }
            upgradeTask.setLog(logAppend.toString());
            upgradeTask.setStatus(UpgradeStatus.FAILURE);
        } finally {
            saveUpgradeTask(upgradeTask);
        }
    }

    public List<PackageInfo> readPackageInfoList() {
        List<PackageInfo> packageInfos = new ArrayList<>();
        try {
            Path packageInfoPath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, Constants.PACKAGE_INFO);
            if (Files.exists(packageInfoPath)) {
                String packageInfosJson = FileUtil.readContent(packageInfoPath.toString());
                if (!ObjectUtils.isEmpty(packageInfosJson)) {
                    packageInfos = JSONObject.parseObject(packageInfosJson, new TypeReference<List<PackageInfo>>() {
                    }.getType());
                }
            }
        } catch (Exception e) {
            log.error("readPackageInfoList failure!! error:" + e);
        }
        return packageInfos;
    }

    public List<UpgradeTask> readUpgradeTaskList() {
        List<UpgradeTask> upgradeTasks = new ArrayList<>();
        try {
            Path upgradeTaskPath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, Constants.UPGRADE_TASK);
            if (!Files.exists(upgradeTaskPath)) {
                upgradeTasks = new ArrayList<>();
            } else {
                String upgradeTasksJson = FileUtil.readContent(upgradeTaskPath.toString());
                if (!ObjectUtils.isEmpty(upgradeTasksJson)) {
                    upgradeTasks = JSONObject.parseObject(upgradeTasksJson, new TypeReference<List<UpgradeTask>>() {
                    }.getType());
                }
            }
            VersionInfo versionInfo = readVersionInfo();
            if (!ObjectUtils.isEmpty(versionInfo) && !ObjectUtils.isEmpty(versionInfo.getId())) {
                boolean contains = false;
                for (UpgradeTask task : upgradeTasks) {
                    if (task.getId().equals(versionInfo.getId())) {
                        task.setStatus(versionInfo.getStatus());
                        task.setLog(versionInfo.getLog());
                        contains = true;
                    }
                }
                if (!contains) {
                    UpgradeTask upgradeTask = UpgradeTask.builder().id(versionInfo.getId()).beforePackageId(versionInfo.getBeforePackageId()).beforeVersion(versionInfo.getBeforeVersion())
                            .afterPackageId(versionInfo.getAfterPackageId()).afterVersion(versionInfo.getAfterVersion())
                            .md5(versionInfo.getMd5()).upgradeTime(versionInfo.getUpgradeTime()).status(versionInfo.getStatus()).log(versionInfo.getLog()).build();
                    upgradeTasks.add(upgradeTask);
                }
            }
        } catch (Exception e) {
            log.error("readUpgradeTaskList failure!! error:" + e);
        }
        return upgradeTasks;
    }

    public VersionInfo readVersionInfo() {
        String userDir = System.getProperty("user.dir");
        VersionInfo versionInfo = VersionInfo.builder().beforeVersion("-").afterVersion("init").afterPath(userDir).build();
        try {
            Path versionInfoPath = Paths.get(userDir, Constants.VERSION_INFO);
            if (Files.exists(versionInfoPath)) {
                String versionInfoJson = FileUtil.readContent(versionInfoPath.toString());
                versionInfo = JSONObject.parseObject(versionInfoJson, new TypeReference<VersionInfo>() {
                }.getType());
                versionInfo.setAfterPath(userDir);
            }
        } catch (Exception e) {
            log.error("readVersionInfo failure!! error:" + e);
        }
        return versionInfo;
    }

    public void savePackageInfo(PackageInfo packageInfo) {
        try {
            boolean contains = false;
            for (int i = 0; i < PACKAGE_INFO_LIST.size(); i++) {
                PackageInfo info = PACKAGE_INFO_LIST.get(i);
                if (info.getId().equals(packageInfo.getId())) {
                    PACKAGE_INFO_LIST.set(i, packageInfo);
                    contains = true;
                }
            }
            if (!contains) {
                PACKAGE_INFO_LIST.add(packageInfo);
            }
            PACKAGE_INFO_MAP.put(packageInfo.getId(), packageInfo);

            Path packageInfoPath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, Constants.PACKAGE_INFO);
            String packageInfosJson = JSONObject.toJSONString(PACKAGE_INFO_LIST);
            if (!ObjectUtils.isEmpty(packageInfosJson)) {
                FileUtil.createAbsolutePathFileWithContent(Collections.singletonList(packageInfosJson), packageInfoPath.toString());
            }
        } catch (Exception e) {
            log.error("savePackageInfo failure!! error:" + e);
        }
    }

    public void saveUpgradeTask(UpgradeTask upgradeTask) {
        try {
            boolean contains = false;
            for (int i = 0; i < UPGRADE_TASK_LIST.size(); i++) {
                UpgradeTask task = UPGRADE_TASK_LIST.get(i);
                if (task.getId().equals(upgradeTask.getId())) {
                    UPGRADE_TASK_LIST.set(i, upgradeTask);
                    contains = true;
                }
            }
            if (!contains) {
                UPGRADE_TASK_LIST.add(upgradeTask);
            }

            Path tempUpgradeTaskPath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, ".tmp", Constants.UPGRADE_TASK);
            Path upgradeTaskPath = Paths.get(Constants.DATA_PATH, Constants.PACKAGE_PATH, Constants.UPGRADE_TASK);
            String upgradeTaskListJson = JSONObject.toJSONString(UPGRADE_TASK_LIST);
            if (!ObjectUtils.isEmpty(upgradeTaskListJson)) {
                FileUtil.createAbsolutePathFileWithContent(Collections.singletonList(upgradeTaskListJson), tempUpgradeTaskPath.toString());
                FileUtil.copyToAbsolutePath(tempUpgradeTaskPath.toString(), upgradeTaskPath.toString());
            }
        } catch (Exception e) {
            log.error("saveUpgradeTask failure!! error:" + e);
        }
    }
}
