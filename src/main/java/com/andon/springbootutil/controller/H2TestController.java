package com.andon.springbootutil.controller;

import com.andon.springbootutil.constant.DataType;
import com.andon.springbootutil.domain.ResponseStandard;
import com.andon.springbootutil.entity.H2Table;
import com.andon.springbootutil.service.TestH2Service;
import io.swagger.annotations.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Andon
 * 2022/10/26
 */
@Api(tags = "H2")
@RestController
@RequestMapping("/h2-test")
@RequiredArgsConstructor
public class H2TestController {

    private final TestH2Service testH2Service;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class H2TableAddVO implements Serializable {
        @NotBlank(message = "名称不能为空")
        @ApiModelProperty(value = "名称", required = true)
        String name;
        @NotNull(message = "数据类型不能为空")
        @ApiModelProperty(value = "数据类型", example = "S", required = true)
        DataType dataType;
    }

    @ApiOperation("新增")
    @PostMapping("/add")
    public ResponseStandard<H2Table> add(@Valid @RequestBody H2TableAddVO h2TableAddVO) {
        return ResponseStandard.successResponse(testH2Service.add(h2TableAddVO));
    }

    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, example = "asd-uio-zxc"),
    })
    @DeleteMapping("/delete")
    public ResponseStandard<Boolean> delete(@RequestParam(name = "id") String id) {
        testH2Service.delete(id);
        return ResponseStandard.successResponse(true);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class H2TableUpdateVO implements Serializable {
        @NotBlank(message = "ID不能为空")
        @ApiModelProperty(value = "ID", required = true)
        String id;
        @NotBlank(message = "名称不能为空")
        @ApiModelProperty(value = "名称", required = true)
        String name;
        @NotNull(message = "数据类型不能为空")
        @ApiModelProperty(value = "数据类型", example = "S", required = true)
        DataType dataType;
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseStandard<H2Table> update(@Valid @RequestBody H2TableUpdateVO h2TableUpdateVO) {
        return ResponseStandard.successResponse(testH2Service.update(h2TableUpdateVO));
    }

    @ApiOperation("查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, example = "asd-uio-zxc"),
    })
    @GetMapping("/query")
    public ResponseStandard<H2Table> query(@RequestParam(name = "id") String id) {
        return ResponseStandard.successResponse(testH2Service.query(id));
    }

    @ApiOperation("查询所有")
    @GetMapping("/queryAll")
    public ResponseStandard<List<H2Table>> queryAll() {
        return testH2Service.queryAll();
    }
}
