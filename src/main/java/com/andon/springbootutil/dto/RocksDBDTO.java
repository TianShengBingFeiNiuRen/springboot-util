package com.andon.springbootutil.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Andon
 * 2021/12/3
 */
@ApiModel("RocksDBVO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RocksDBDTO implements Serializable {

    @NonNull
    @ApiModelProperty(value = "列族", required = true)
    @Builder.Default
    private String cfName = "default";
    @NonNull
    @ApiModelProperty(value = "键", required = true)
    private String key;
    @ApiModelProperty(value = "值", required = true)
    private String value;
}
