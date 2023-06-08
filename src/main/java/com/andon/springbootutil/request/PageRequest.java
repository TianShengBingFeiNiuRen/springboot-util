package com.andon.springbootutil.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;

/**
 * @author Andon
 * 2023/5/17
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PageRequest {

    @Min(value = 1, message = "页码不能小于 1")
    @ApiModelProperty(value = "页码，从1开始计数", example = "1")
    int page = 1;

    @Min(value = 1, message = "页大小不能小于 1")
    @ApiModelProperty(value = "页大小", example = "10")
    int size = 10;

    public int offset() {
        return (this.page - 1) * this.page;
    }
}
