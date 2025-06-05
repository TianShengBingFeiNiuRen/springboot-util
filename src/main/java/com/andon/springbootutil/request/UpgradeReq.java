package com.andon.springbootutil.request;

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
public class UpgradeReq implements Serializable {

    @NotEmpty(message = "升级包ID不能为空")
    @ApiModelProperty(value = "升级包ID", required = true)
    private String packageId;

    @NotNull(message = "是否立即升级不能为空")
    @ApiModelProperty(value = "是否立即升级", example = "TRUE", required = true)
    private Boolean immediately;

    @ApiModelProperty(value = "升级时间：yyyy-MM-dd HH:mm:ss")
    String upgradeTime;
}
