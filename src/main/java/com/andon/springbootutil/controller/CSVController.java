package com.andon.springbootutil.controller;

import com.andon.springbootutil.util.CSVUtil;
import com.andon.springbootutil.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

/**
 * @author Andon
 * 2021/12/21
 */
@Slf4j
@Api(tags = "CSV")
@RequestMapping("/csv")
@RestController
public class CSVController {

    @ApiOperation("上传")
    @PostMapping(value = "/upload")
    public List<String> upload(MultipartFile multipartFile) throws IOException {
        String save = FileUtil.save(multipartFile, UUID.randomUUID().toString(), "HelloWorld.tmp");
        File file = CSVUtil.uploadFile(multipartFile);
        assert file != null;
        long count = CSVUtil.readDataCount(file.getPath());
        log.info("文件-> [{}] count={}", file.getPath(), count);
        List<String> list = CSVUtil.readCSVToList(file.getPath(), null);
        boolean delete = file.delete();
        boolean parentFileDelete = file.getParentFile().delete();
        return list;
    }

    @ApiOperation("下载")
    @GetMapping(value = "/download")
    public void download(String fileName, String head, String values, HttpServletResponse httpServletResponse) throws IOException {
        String[] headArr = head.split(",");
        List<String[]> valueList = new ArrayList<>(headArr.length + 1);
        String[] valueArr = values.split("\\|");
        for (String value : valueArr) {
            valueList.add(value.split(","));
        }
        fileName += "_" + System.currentTimeMillis();
        File file = CSVUtil.makeTempCSV(fileName, headArr, valueList);
        boolean b = FileUtil.downloadFile(httpServletResponse, file, fileName+".csv");
    }
}
