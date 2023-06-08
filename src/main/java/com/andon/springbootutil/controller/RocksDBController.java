package com.andon.springbootutil.controller;

import com.andon.springbootutil.response.CommonResponse;
import com.andon.springbootutil.dto.RocksDBDTO;
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
    public CommonResponse<String> cfAdd(String cfName) throws RocksDBException {
        RocksDBUtil.cfAddIfNotExist(cfName);
        return CommonResponse.successResponse(cfName);
    }

    @ApiOperation("列族，删除（如果存在）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @DeleteMapping("/cf")
    public CommonResponse<String> cfDelete(String cfName) throws RocksDBException {
        RocksDBUtil.cfDeleteIfExist(cfName);
        return CommonResponse.successResponse(cfName);
    }

    @ApiOperation("列族名（查询所有）")
    @GetMapping("/cf-all")
    public CommonResponse<Set<String>> cfAll() {
        Set<String> cfNames = RocksDBUtil.columnFamilyHandleMap.keySet();
        CommonResponse<Set<String>> response = CommonResponse.successResponse(cfNames);
        response.setTotal(cfNames.size());
        return response;
    }

    @ApiOperation("增")
    @PostMapping("/put")
    public CommonResponse<RocksDBDTO> put(@RequestBody RocksDBDTO rocksDBDTO) throws RocksDBException {
        RocksDBUtil.put(rocksDBDTO.getCfName(), rocksDBDTO.getKey(), rocksDBDTO.getValue());
        return CommonResponse.successResponse(rocksDBDTO);
    }

    @ApiOperation("增（批量）")
    @PostMapping("/batch-put")
    public CommonResponse<List<RocksDBDTO>> batchPut(@RequestBody List<RocksDBDTO> rocksDBDTOS) throws RocksDBException {
        Map<String, String> map = new HashMap<>();
        for (RocksDBDTO rocksDBDTO : rocksDBDTOS) {
            map.put(rocksDBDTO.getKey(), rocksDBDTO.getValue());
        }
        RocksDBUtil.batchPut(rocksDBDTOS.get(0).getCfName(), map);
        CommonResponse<List<RocksDBDTO>> response = CommonResponse.successResponse(rocksDBDTOS);
        response.setTotal(rocksDBDTOS.size());
        return response;
    }

    @ApiOperation("删")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
            @ApiImplicitParam(name = "key", value = "键", required = true),
    })
    @DeleteMapping("/delete")
    public CommonResponse<RocksDBDTO> delete(String cfName, String key) throws RocksDBException {
        String value = RocksDBUtil.get(cfName, key);
        RocksDBUtil.delete(cfName, key);
        RocksDBDTO rocksDBDTO = RocksDBDTO.builder().cfName(cfName).key(key).value(value).build();
        return CommonResponse.successResponse(rocksDBDTO);
    }

    @ApiOperation("查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
            @ApiImplicitParam(name = "key", value = "键", required = true),
    })
    @GetMapping("/get")
    public CommonResponse<RocksDBDTO> get(String cfName, String key) throws RocksDBException {
        String value = RocksDBUtil.get(cfName, key);
        RocksDBDTO rocksDBDTO = RocksDBDTO.builder().cfName(cfName).key(key).value(value).build();
        return CommonResponse.successResponse(rocksDBDTO);
    }

    @ApiOperation("查（多个键值对）")
    @PostMapping("/multiGetAsList")
    public CommonResponse<List<RocksDBDTO>> multiGetAsList(@RequestBody List<RocksDBDTO> rocksDBDTOS) throws RocksDBException {
        List<RocksDBDTO> list = new ArrayList<>();
        String cfName = rocksDBDTOS.get(0).getCfName();
        List<String> keys = new ArrayList<>(rocksDBDTOS.size());
        for (RocksDBDTO rocksDBDTO : rocksDBDTOS) {
            keys.add(rocksDBDTO.getKey());
        }
        Map<String, String> map = RocksDBUtil.multiGetAsMap(cfName, keys);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            RocksDBDTO rocksDBDTO = RocksDBDTO.builder().cfName(cfName).key(entry.getKey()).value(entry.getValue()).build();
            list.add(rocksDBDTO);
        }
        CommonResponse<List<RocksDBDTO>> response = CommonResponse.successResponse(list);
        response.setTotal(list.size());
        return response;
    }

    @ApiOperation("查所有键值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @GetMapping("/get-all")
    public CommonResponse<List<RocksDBDTO>> getAll(String cfName) throws RocksDBException {
        List<RocksDBDTO> rocksDBDTOS = new ArrayList<>();
        Map<String, String> all = RocksDBUtil.getAll(cfName);
        for (Map.Entry<String, String> entry : all.entrySet()) {
            RocksDBDTO rocksDBDTO = RocksDBDTO.builder().cfName(cfName).key(entry.getKey()).value(entry.getValue()).build();
            rocksDBDTOS.add(rocksDBDTO);
        }
        CommonResponse<List<RocksDBDTO>> response = CommonResponse.successResponse(rocksDBDTOS);
        response.setTotal(rocksDBDTOS.size());
        return response;
    }

    @ApiOperation("分片查（键）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @GetMapping("/get-keys")
    public CommonResponse<List<String>> getKeysFrom(String cfName) throws RocksDBException {
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
        CommonResponse<List<String>> response = CommonResponse.successResponse(data);
        response.setTotal(data.size());
        return response;
    }

    @ApiOperation("查（所有键）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @GetMapping("/get-all-key")
    public CommonResponse<List<String>> getAllKey(String cfName) throws RocksDBException {
        List<String> allKey = RocksDBUtil.getAllKey(cfName);
        CommonResponse<List<String>> response = CommonResponse.successResponse(allKey);
        response.setTotal(allKey.size());
        return response;
    }

    @ApiOperation("查总条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cfName", value = "列族", required = true),
    })
    @GetMapping("/get-count")
    public CommonResponse<Integer> getCount(String cfName) throws RocksDBException {
        int count = RocksDBUtil.getCount(cfName);
        return CommonResponse.successResponse(count);
    }
}
