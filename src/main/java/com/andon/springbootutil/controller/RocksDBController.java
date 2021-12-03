package com.andon.springbootutil.controller;

import com.andon.springbootutil.domain.ResponseStandard;
import com.andon.springbootutil.util.RocksDBUtil;
import com.andon.springbootutil.vo.RocksDBVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.rocksdb.RocksDBException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andon
 * 2021/12/3
 */
@SuppressWarnings("DuplicatedCode")
@Api(tags = "RocksDB")
@RestController
@RequestMapping(value = "/rocksdb")
public class RocksDBController {

    @ApiOperation("增")
    @PostMapping("/put")
    public ResponseStandard<RocksDBVo> put(@RequestBody RocksDBVo rocksDBVo) throws RocksDBException {
        RocksDBUtil.put(rocksDBVo.getKey(), rocksDBVo.getValue());
        return ResponseStandard.successResponse(rocksDBVo);
    }

    @ApiOperation("增-batch")
    @PostMapping("/batch-put")
    public ResponseStandard<List<RocksDBVo>> batchPut(@RequestBody List<RocksDBVo> rocksDBVos) throws RocksDBException {
        Map<String, String> map = new HashMap<>();
        for (RocksDBVo rocksDBVo : rocksDBVos) {
            map.put(rocksDBVo.getKey(), rocksDBVo.getValue());
        }
        RocksDBUtil.batchPut(map);
        ResponseStandard<List<RocksDBVo>> response = ResponseStandard.successResponse(rocksDBVos);
        response.setTotal(rocksDBVos.size());
        return response;
    }

    @ApiOperation("删")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "键", required = true),
    })
    @DeleteMapping("/delete")
    public ResponseStandard<RocksDBVo> delete(String key) throws RocksDBException {
        String value = RocksDBUtil.get(key);
        RocksDBUtil.delete(key);
        RocksDBVo rocksDBVo = RocksDBVo.builder().key(key).value(value).build();
        return ResponseStandard.successResponse(rocksDBVo);
    }

    @ApiOperation("查-get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "键", required = true),
    })
    @GetMapping("/get")
    public ResponseStandard<RocksDBVo> get(String key) throws RocksDBException {
        String value = RocksDBUtil.get(key);
        RocksDBVo rocksDBVo = RocksDBVo.builder().key(key).value(value).build();
        return ResponseStandard.successResponse(rocksDBVo);
    }

    @ApiOperation("查-multiGetAsList")
    @PostMapping("/multiGetAsList")
    public ResponseStandard<List<RocksDBVo>> multiGetAsList(@RequestBody List<String> keys) throws RocksDBException {
        List<RocksDBVo> rocksDBVos = new ArrayList<>();
        Map<String, String> map = RocksDBUtil.multiGetAsList(keys);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            RocksDBVo rocksDBVo = RocksDBVo.builder().key(entry.getKey()).value(entry.getValue()).build();
            rocksDBVos.add(rocksDBVo);
        }
        ResponseStandard<List<RocksDBVo>> response = ResponseStandard.successResponse(rocksDBVos);
        response.setTotal(rocksDBVos.size());
        return response;
    }

    @ApiOperation("查-getAll")
    @GetMapping("/get-all")
    public ResponseStandard<List<RocksDBVo>> getAll() {
        List<RocksDBVo> rocksDBVos = new ArrayList<>();
        Map<String, String> all = RocksDBUtil.getAll();
        for (Map.Entry<String, String> entry : all.entrySet()) {
            RocksDBVo rocksDBVo = RocksDBVo.builder().key(entry.getKey()).value(entry.getValue()).build();
            rocksDBVos.add(rocksDBVo);
        }
        ResponseStandard<List<RocksDBVo>> response = ResponseStandard.successResponse(rocksDBVos);
        response.setTotal(rocksDBVos.size());
        return response;
    }
}
