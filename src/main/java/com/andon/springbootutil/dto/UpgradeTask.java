package com.andon.springbootutil.dto;

import com.andon.springbootutil.constant.UpgradeStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Andon
 * 2025/5/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeTask implements Serializable {

    @ApiModelProperty(value = "ID")
    private String id;

    // TODO 模块

    @ApiModelProperty(value = "升级前包ID")
    private String beforePackageId;

    @ApiModelProperty(value = "升级前版本")
    private String beforeVersion;

    @ApiModelProperty(value = "升级后包ID")
    private String afterPackageId;

    @ApiModelProperty(value = "升级后版本")
    private String afterVersion;

    @ApiModelProperty(value = "MD5")
    private String md5;

    // TODO 来源

    @ApiModelProperty(value = "升级时间")
    private String upgradeTime;

    @ApiModelProperty(value = "升级状态：WAIT（待升级）UPGRADING（升级中）SUCCESS（升级成功）FAILURE（升级失败）INVALID（已失效）")
    private UpgradeStatus status;

    @ApiModelProperty(value = "日志")
    private String log;
}
