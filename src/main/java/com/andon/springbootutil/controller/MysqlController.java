package com.andon.springbootutil.controller;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.mapper.DatabaseMapper;
import com.andon.springbootutil.response.CommonResponse;
import com.andon.springbootutil.util.FileUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andon
 * 2023/11/20
 */
@Slf4j
@Api(tags = "MySQL")
@RestController
@RequiredArgsConstructor
public class MysqlController {

    private final DatabaseMapper databaseMapper;

    @PostMapping(value = "/importCsvToMySQL")
    public CommonResponse<Boolean> importCsvToMySQL(MultipartFile file, String tableName) throws Exception {
        String fineName = tableName == null ? file.getName() : tableName;
        String filePath = FileUtil.save(file, fineName.replaceAll(".*[./*~!@#$%^&?\\-_=+\\[\\]{}].*", "") + "_" + System.currentTimeMillis() + System.currentTimeMillis(), fineName + ".csv");
        String firstLine = FileUtil.readFirstLine(filePath);
        List<String> schema = Arrays.asList(firstLine.split(","));
        log.info("schema:{}", JSONObject.toJSONString(schema));
        String table = file.getName().split("\\.")[0];

        tableName = tableName + "_" + System.currentTimeMillis();
        databaseMapper.dropTable(tableName);
        databaseMapper.createTable(tableName, table);
        for (String column : schema) {
            databaseMapper.addColumn(tableName, column);
        }
        AtomicInteger row = new AtomicInteger();
        String column = String.format("`%s`", String.join("`,`", schema));
        int batchSize = 1000;
        List<String> lines = new ArrayList<>(batchSize);
        String finalTableName = tableName;
        FileUtil.readFileContentByLine(filePath, (bufferedReader) -> {
            String line;
            boolean first = true;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                    if (first) {
                        first = false;
                        continue;
                    }
                    lines.add(line);
                    if (lines.size() == batchSize) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < lines.size(); i++) {
                            List<String> lineColumn = Arrays.asList(lines.get(i).split(","));
                            if (lineColumn.size() != schema.size()) {
                                log.warn("跳过非法行 schema.size={} column.size={}，lineColumn:{}", schema.size(), lineColumn.size(), JSONObject.toJSONString(lineColumn));
                                continue;
                            }
                            if (i != 0) {
                                stringBuilder.append(",");
                            }
                            String values = String.join("','", lineColumn);
                            stringBuilder.append(String.format("('%s')", values));
                        }
                        databaseMapper.insertInto(finalTableName, column, stringBuilder.toString());
                        row.addAndGet(lines.size());
                        log.info("tableName:{} row:{}", finalTableName, row.get());
                        lines.clear();
                    }
                } catch (Exception e) {
                    break;
                }
            }
            if (!ObjectUtils.isEmpty(lines)) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < lines.size(); i++) {
                    List<String> lineColumn = Arrays.asList(lines.get(i).split(","));
                    if (lineColumn.size() != schema.size()) {
                        log.warn("跳过非法行 schema.size={} column.size={}，lineColumn:{}", schema.size(), lineColumn.size(), JSONObject.toJSONString(lineColumn));
                        continue;
                    }
                    if (i != 0) {
                        stringBuilder.append(",");
                    }
                    String values = String.join("','", lineColumn);
                    stringBuilder.append(String.format("('%s')", values));
                }
                databaseMapper.insertInto(finalTableName, column, stringBuilder.toString());
                row.addAndGet(lines.size());
                log.info("tableName:{} row:{}", finalTableName, row.get());
            }
        });
        CommonResponse<Boolean> response = CommonResponse.successResponse(true, row.get());
        response.setMessage("tableName:" + tableName);
        return response;
    }
}
