package com.andon.springbootutil.controller;

import com.andon.springbootutil.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Andon
 * 2024/11/1
 */
@Slf4j
@Api(tags = "Excel")
@RestController
@RequestMapping(value = "/excel")
public class ExcelController {

    @ApiOperation("导出")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse httpServletResponse) throws IOException {
        List<List<String>> data = new ArrayList<>();
        List<String> header = Arrays.asList("a", "b", "c");
        data.add(header);
        for (int i = 0; i < 65535; i++) {
            data.add(Arrays.asList(String.valueOf((i + 1)), String.valueOf(System.currentTimeMillis()), new Date().toString()));
        }
        ExcelUtil.exportExcel(httpServletResponse, data, "log", "操作日志_" + System.currentTimeMillis());
    }
}
