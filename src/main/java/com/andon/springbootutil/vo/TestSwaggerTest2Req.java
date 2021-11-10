package com.andon.springbootutil.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * test2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSwaggerTest2Req implements Serializable {

    @NonNull
    @ApiModelProperty(value = "param1", required = true)
    private String param1;
    @NonNull
    @ApiModelProperty(value = "param2", required = true)
    private String param2;
}
