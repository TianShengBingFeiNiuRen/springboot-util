package com.andon.springbootutil.controller;

import com.andon.springbootutil.util.FileUtil;
import com.andon.springbootutil.util.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author Andon
 * 2022/9/7
 */

@Slf4j
@Api(tags = "ZIP")
@RequestMapping("/zip")
@RestController
public class ZipController {

    @ApiOperation("压缩下载")
    @GetMapping(value = "/zip-download")
    public void zipDownload(@RequestParam String zipFileName, @ApiIgnore HttpServletResponse httpServletResponse) {
        zipFileName = String.format("%s_%s.zip", zipFileName, System.currentTimeMillis());
        String inputFilePath = "F:\\安恒\\MPC\\MPC 1.0.3\\test";
        File zipFile = ZipUtil.zipFilePath(inputFilePath, zipFileName);
        boolean b = FileUtil.downloadFile(httpServletResponse, zipFile, zipFileName);
    }
}
