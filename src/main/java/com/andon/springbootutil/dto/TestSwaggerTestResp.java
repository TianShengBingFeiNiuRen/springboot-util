package com.andon.springbootutil.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * test
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSwaggerTestResp implements Serializable {

    @ApiModelProperty(value = "param1")
    private String param1;
    @ApiModelProperty(value = "param2")
    private String param2;
}
