package com.andon.springbootutil.dto;

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
public class PackageInfo implements Serializable {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    // TODO 模块

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "MD5")
    private String md5;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "上传时间")
    private String uploadTime;

}
