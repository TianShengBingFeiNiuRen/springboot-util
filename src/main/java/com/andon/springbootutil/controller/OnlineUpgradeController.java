package com.andon.springbootutil.controller;

import com.andon.springbootutil.dto.PackageInfo;
import com.andon.springbootutil.dto.UpgradeTask;
import com.andon.springbootutil.dto.VersionInfo;
import com.andon.springbootutil.request.PackageUploadReq;
import com.andon.springbootutil.request.UpgradeReq;
import com.andon.springbootutil.response.CommonResponse;
import com.andon.springbootutil.service.OnlineUpgradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Andon
 * 2025/5/27
 */
@Slf4j
@Api(tags = "OnlineUpgrade")
@RequestMapping("/online-upgrade")
@RestController
@RequiredArgsConstructor
public class OnlineUpgradeController {

    private final OnlineUpgradeService onlineUpgradeService;

    @ApiOperation("包管理-上传")
    @PostMapping(value = "/package/upload")
    public CommonResponse<String> packageUpload(MultipartFile file) throws Exception {
        String id = onlineUpgradeService.packageUpload(file);
        return CommonResponse.successResponse(id);
    }

    @ApiOperation("包管理-创建")
    @PostMapping(value = "/package/create")
    public CommonResponse<String> packageCreate(@Valid @RequestBody PackageUploadReq packageUploadReq) throws Exception {
        String id = onlineUpgradeService.packageCreate(packageUploadReq);
        return CommonResponse.successResponse(id);
    }

    @ApiOperation("包管理-列表")
    @GetMapping(value = "/package/list")
    public CommonResponse<List<PackageInfo>> packageList() {
        List<PackageInfo> packageInfos = new ArrayList<>(OnlineUpgradeService.PACKAGE_INFO_LIST);
        Collections.reverse(packageInfos);
        return CommonResponse.successResponse(packageInfos, packageInfos.size());
    }

    @ApiOperation("当前节点版本信息")
    @GetMapping(value = "/version-info")
    public CommonResponse<VersionInfo> versionInfo() {
        return CommonResponse.successResponse(OnlineUpgradeService.VERSION_INFO);
    }

    @ApiOperation("节点升级")
    @PostMapping(value = "/upgrade")
    public CommonResponse<String> upgrade(@Valid @RequestBody UpgradeReq upgradeReq) throws Exception {
        String id = onlineUpgradeService.upgrade(upgradeReq);
        return CommonResponse.successResponse(id);
    }

    @ApiOperation("节点升级-列表")
    @GetMapping(value = "/upgrade/list")
    public CommonResponse<List<UpgradeTask>> upgradeList(int fromIndex, int toIndex) {
        List<UpgradeTask> upgradeTasks = new ArrayList<>(OnlineUpgradeService.UPGRADE_TASK_LIST);
        Collections.reverse(upgradeTasks);
        // TODO 日志量比较大，生产不建议列表返回
        return CommonResponse.successResponse(upgradeTasks.subList(fromIndex, toIndex == 0 ? 5 : toIndex), upgradeTasks.size());
    }
}
