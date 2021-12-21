package com.andon.springbootutil.controller;

import com.andon.springbootutil.util.CSVUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andon
 * 2021/12/21
 */
@Api(tags = "CSV")
@RequestMapping("/csv")
@RestController
public class CSVController {

    @ApiOperation("上传")
    @PostMapping(value = "/upload")
    public List<List<String>> upload(MultipartFile multipartFile) {
        File file = CSVUtil.uploadFile(multipartFile);
        assert file != null;
        return CSVUtil.readCSV(file.getPath());
    }

    @ApiOperation("下载")
    @GetMapping(value = "/download")
    public String download(String fileName, String head, String values, HttpServletResponse httpServletResponse) throws IOException {
        String[] headArr = head.split(",");
        List<String[]> valueList = new ArrayList<>(headArr.length + 1);
        String[] valueArr = values.split("\\|");
        for (String value : valueArr) {
            valueList.add(value.split(","));
        }
        File file = CSVUtil.makeTempCSV(fileName, headArr, valueList);
        CSVUtil.downloadFile(httpServletResponse, file, fileName);
        return null;
    }
}
