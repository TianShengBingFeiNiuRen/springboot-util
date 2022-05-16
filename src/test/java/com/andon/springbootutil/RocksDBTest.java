package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.RocksDBUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Andon
 * 2022/5/13
 */
@Slf4j
public class RocksDBTest {

    @Test
    public void test02() throws RocksDBException {
        String cfName = "dataset0";
        List<String> allKey = RocksDBUtil.getAllKey(cfName);
        log.info("cfName:{} allKey:{} size={}", cfName, JSONObject.toJSONString(allKey), allKey.size());

        List<String> keys;
        String lastKey = null;
        int i = 0;
        while (true) {
            keys = RocksDBUtil.getKeysFrom(cfName, lastKey);
            if (keys.isEmpty()) {
                break;
            }
            i += 1;
            lastKey = keys.get(keys.size() - 1);
            log.info("i:{} lastKey:{} keys:{}", i, lastKey, JSONObject.toJSONString(keys));
            keys.clear();
        }
    }

    @Test
    public void test01() throws RocksDBException {
        ConcurrentMap<String, ColumnFamilyHandle> columnFamilyHandleMap = RocksDBUtil.columnFamilyHandleMap;
        Set<String> cfNames = columnFamilyHandleMap.keySet();
        log.info("cfNames:{}", JSONObject.toJSONString(cfNames));
        for (String cfName : cfNames) {
            List<String> allKey = RocksDBUtil.getAllKey(cfName);
            log.info("cfName:{} allKey:{} size={}", cfName, JSONObject.toJSONString(allKey), allKey.size());
        }
    }
}
