package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.util.HashMap;
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
     * put
     */
    public static void put(String key, String value) throws RocksDBException {
        rocksDB.put(key.getBytes(), value.getBytes());
    }

    /**
     * get
     */
    public static String get(String key) throws RocksDBException {
        byte[] bytes = rocksDB.get(key.getBytes());
        return new String(bytes);
    }

    /**
     * getAll
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
