package com.andon.springbootutil.request;

import com.andon.springbootutil.constant.UploadMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Andon
 * 2025/5/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageUploadReq implements Serializable {

    @ApiModelProperty(value = "名称")
    private String name;

    @NotEmpty(message = "版本不能为空")
    @ApiModelProperty(value = "版本", example = "V1.0", required = true)
    private String version;

    @ApiModelProperty(value = "描述")
    private String desc;

    @NotNull(message = "上传方式不能为空")
    @ApiModelProperty(value = "上传方式：WEB（页面）SERVER（服务器）", example = "WEB", required = true)
    private UploadMethod uploadMethod;

    @ApiModelProperty(value = "升级包文件ID")
    private String fileId;

    @ApiModelProperty(value = "服务器文件路径")
    private String serverFilePath;

    // TODO 私钥加密md5，后端校验
}
