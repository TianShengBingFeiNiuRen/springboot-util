package com.andon.springbootutil.controller;

import com.andon.springbootutil.domain.ResponseStandard;
import com.andon.springbootutil.dto.RocksDBVo;
import com.andon.springbootutil.util.RocksDBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.rocksdb.RocksDBException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Andon
 * 2021/12/3
 */
@SuppressWarnings("DuplicatedCode")
@Api(tags = "RocksDB")
@RestController
@RequestMapping(value = "/rocksdb")
public class RocksDBController {

    @ApiOperation("列族，创建（如果不存在）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @PostMapping("/cf")
    public ResponseStandard<String> cfAdd(String cfName) throws RocksDBException {
        RocksDBUtil.cfAddIfNotExist(cfName);
        return ResponseStandard.successResponse(cfName);
    }

    @ApiOperation("列族，删除（如果存在）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @DeleteMapping("/cf")
    public ResponseStandard<String> cfDelete(String cfName) throws RocksDBException {
        RocksDBUtil.cfDeleteIfExist(cfName);
        return ResponseStandard.successResponse(cfName);
    }

    @ApiOperation("列族名（查询所有）")
    @GetMapping("/cf-all")
    public ResponseStandard<Set<String>> cfAll() {
        Set<String> cfNames = RocksDBUtil.columnFamilyHandleMap.keySet();
        ResponseStandard<Set<String>> response = ResponseStandard.successResponse(cfNames);
        response.setTotal(cfNames.size());
        return response;
    }

    @ApiOperation("增")
    @PostMapping("/put")
    public ResponseStandard<RocksDBVo> put(@RequestBody RocksDBVo rocksDBVo) throws RocksDBException {
        RocksDBUtil.put(rocksDBVo.getCfName(), rocksDBVo.getKey(), rocksDBVo.getValue());
        return ResponseStandard.successResponse(rocksDBVo);
    }

    @ApiOperation("增（批量）")
    @PostMapping("/batch-put")
    public ResponseStandard<List<RocksDBVo>> batchPut(@RequestBody List<RocksDBVo> rocksDBVos) throws RocksDBException {
        Map<String, String> map = new HashMap<>();
        for (RocksDBVo rocksDBVo : rocksDBVos) {
            map.put(rocksDBVo.getKey(), rocksDBVo.getValue());
        }
        RocksDBUtil.batchPut(rocksDBVos.get(0).getCfName(), map);
        ResponseStandard<List<RocksDBVo>> response = ResponseStandard.successResponse(rocksDBVos);
        response.setTotal(rocksDBVos.size());
        return response;
    }

    @ApiOperation("删")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
            @ApiImplicitParam(name = "key", value = "键", required = true),
    })
    @DeleteMapping("/delete")
    public ResponseStandard<RocksDBVo> delete(String cfName, String key) throws RocksDBException {
        String value = RocksDBUtil.get(cfName, key);
        RocksDBUtil.delete(cfName, key);
        RocksDBVo rocksDBVo = RocksDBVo.builder().cfName(cfName).key(key).value(value).build();
        return ResponseStandard.successResponse(rocksDBVo);
    }

    @ApiOperation("查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
            @ApiImplicitParam(name = "key", value = "键", required = true),
    })
    @GetMapping("/get")
    public ResponseStandard<RocksDBVo> get(String cfName, String key) throws RocksDBException {
        String value = RocksDBUtil.get(cfName, key);
        RocksDBVo rocksDBVo = RocksDBVo.builder().cfName(cfName).key(key).value(value).build();
        return ResponseStandard.successResponse(rocksDBVo);
    }

    @ApiOperation("查（多个键值对）")
    @PostMapping("/multiGetAsList")
    public ResponseStandard<List<RocksDBVo>> multiGetAsList(@RequestBody List<RocksDBVo> rocksDBVos) throws RocksDBException {
        List<RocksDBVo> list = new ArrayList<>();
        String cfName = rocksDBVos.get(0).getCfName();
        List<String> keys = new ArrayList<>(rocksDBVos.size());
        for (RocksDBVo rocksDBVo : rocksDBVos) {
            keys.add(rocksDBVo.getKey());
        }
        Map<String, String> map = RocksDBUtil.multiGetAsMap(cfName, keys);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            RocksDBVo rocksDBVo = RocksDBVo.builder().cfName(cfName).key(entry.getKey()).value(entry.getValue()).build();
            list.add(rocksDBVo);
        }
        ResponseStandard<List<RocksDBVo>> response = ResponseStandard.successResponse(list);
        response.setTotal(list.size());
        return response;
    }

    @ApiOperation("查所有键值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @GetMapping("/get-all")
    public ResponseStandard<List<RocksDBVo>> getAll(String cfName) throws RocksDBException {
        List<RocksDBVo> rocksDBVos = new ArrayList<>();
        Map<String, String> all = RocksDBUtil.getAll(cfName);
        for (Map.Entry<String, String> entry : all.entrySet()) {
            RocksDBVo rocksDBVo = RocksDBVo.builder().cfName(cfName).key(entry.getKey()).value(entry.getValue()).build();
            rocksDBVos.add(rocksDBVo);
        }
        ResponseStandard<List<RocksDBVo>> response = ResponseStandard.successResponse(rocksDBVos);
        response.setTotal(rocksDBVos.size());
        return response;
    }

    @ApiOperation("分片查（键）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @GetMapping("/get-keys")
    public ResponseStandard<List<String>> getKeysFrom(String cfName) throws RocksDBException {
        List<String> data = new ArrayList<>();
        List<String> keys;
        String lastKey = null;
        while (true) {
            keys = RocksDBUtil.getKeysFrom(cfName, lastKey);
            if (keys.isEmpty()) {
                break;
            }
            lastKey = keys.get(keys.size() - 1);
            data.addAll(keys);
            keys.clear();
        }
        ResponseStandard<List<String>> response = ResponseStandard.successResponse(data);
        response.setTotal(data.size());
        return response;
    }

    @ApiOperation("查（所有键）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @GetMapping("/get-all-key")
    public ResponseStandard<List<String>> getAllKey(String cfName) throws RocksDBException {
        List<String> allKey = RocksDBUtil.getAllKey(cfName);
        ResponseStandard<List<String>> response = ResponseStandard.successResponse(allKey);
        response.setTotal(allKey.size());
        return response;
    }

    @ApiOperation("查总条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @GetMapping("/get-count")
    public ResponseStandard<Integer> getCount(String cfName) throws RocksDBException {
        int count = RocksDBUtil.getCount(cfName);
        return ResponseStandard.successResponse(count);
    }
}
