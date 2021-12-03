package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andon
 * 2021/12/2
 * <p>
 * 操作 RocksDB
 */
@Slf4j
public class RocksDBUtil {

    private static RocksDB rocksDB;

    /*
      初始化 RocksDB
     */
    static {
        try {
            String osName = System.getProperty("os.name");
            log.info("osName:{}", osName);
            String rocksDBPath; //RocksDB文件目录
            if (osName.toLowerCase().contains("windows")) {
                rocksDBPath = "D:\\RocksDB"; // 指定windows系统下RocksDB文件目录
            } else {
                rocksDBPath = "/usr/local/rocksdb"; // 指定linux系统下RocksDB文件目录
            }
            RocksDB.loadLibrary();
            Options options = new Options();
            options.setCreateIfMissing(true); //如果数据库不存在则创建
            rocksDB = RocksDB.open(options, rocksDBPath);
            log.info("RocksDB init success!! path:{}", rocksDBPath);
        } catch (Exception e) {
            log.info("RocksDB init failure!! error:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 增
     */
    public static void put(String key, String value) throws RocksDBException {
        rocksDB.put(key.getBytes(), value.getBytes());
    }

    /**
     * 批量增
     */
    public static void batchPut(Map<String, String> map) throws RocksDBException {
        WriteOptions writeOptions = new WriteOptions();
        WriteBatch writeBatch = new WriteBatch();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            writeBatch.put(entry.getKey().getBytes(), entry.getValue().getBytes());
        }
        rocksDB.write(writeOptions, writeBatch);
    }

    /**
     * 删
     */
    public static void delete(String key) throws RocksDBException {
        rocksDB.delete(key.getBytes());
    }

    /**
     * 查
     */
    public static String get(String key) throws RocksDBException {
        byte[] bytes = rocksDB.get(key.getBytes());
        return new String(bytes);
    }

    /**
     * 查-多个键值对
     */
    public static Map<String, String> multiGetAsList(List<String> keys) throws RocksDBException {
        Map<String, String> map = new HashMap<>();
        List<byte[]> keyBytes = new ArrayList<>();
        for (String key : keys) {
            keyBytes.add(key.getBytes());
        }
        List<byte[]> bytes = rocksDB.multiGetAsList(keyBytes);
        for (int i = 0; i < bytes.size(); i++) {
            byte[] valueBytes = bytes.get(i);
            String value = null;
            if (!ObjectUtils.isEmpty(valueBytes)) {
                value = new String(valueBytes);
            }
            map.put(keys.get(i), value);
        }
        return map;
    }

    /**
     * 查所有键值对
     */
    public static Map<String, String> getAll() {
        Map<String, String> map = new HashMap<>();
        RocksIterator rocksIterator = rocksDB.newIterator();
        for (rocksIterator.seekToFirst(); rocksIterator.isValid(); rocksIterator.next()) {
            map.put(new String(rocksIterator.key()), new String(rocksIterator.value()));
        }
        return map;
    }
}
