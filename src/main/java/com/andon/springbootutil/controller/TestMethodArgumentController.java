package com.andon.springbootutil.controller;

import com.andon.springbootutil.domain.ResponseStandard;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Andon
 * 2022/7/12
 */
@RestController
@RequestMapping(value = "/test")
public class TestMethodArgumentController {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class JsonFormatVO implements Serializable {
        @NotNull(message = "日期不能为空")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
        @ApiModelProperty(value = "日期", example = "2022-07-12 19:30")
        private Date date;
        @Valid
        @JsonUnwrapped
        private JsonFormatVO2 jsonFormatVO2;
        @JsonIgnore
        private JsonFormatVO3 jsonFormatVO3;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class JsonFormatVO2 implements Serializable {
        @NotBlank(message = "key不能为空")
        @ApiModelProperty(value = "键")
        private String key;
        @ApiModelProperty(value = "值")
        private String value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class JsonFormatVO3 implements Serializable {
        private String name;
        private String age;
    }

    @PostMapping("/method-argument")
    public ResponseStandard<JsonFormatVO> methodArgument(@RequestBody @Valid JsonFormatVO jsonFormatVO) {
        return ResponseStandard.successResponse(jsonFormatVO);
    }
}
