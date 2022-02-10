package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.*;
import com.andon.springbootutil.vo.RocksDBVo;
import com.andon.springbootutil.vo.TestSwaggerTestResp;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
public class Demo {

    @Test
    public void test31() {
        RocksDBVo rocksDBVo = RocksDBVo.builder().cfName("test_cf_name").key("test_key").value("test_value").build();
        RocksDBVo vo = new RocksDBVo();
        log.info("rocksDBVo:{}", JSONObject.toJSONString(rocksDBVo));
        log.info("vo:{}", JSONObject.toJSONString(vo));
        BeanUtils.copyProperties(rocksDBVo, vo);
        log.info("vo:{}", JSONObject.toJSONString(vo));
        System.out.println(rocksDBVo == vo);
    }

    @Test
    public void test30() {
        String str = "/andon/query";
        boolean matches = str.matches("/andon/.*");
        log.info("matches:{}", matches);

        String substring = str.substring(str.lastIndexOf("/") + 1);
        log.info("substring:{}", substring);
    }

    @Test
    public void test29() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        log.info("list:{}", list);
        Collections.shuffle(list);
        log.info("list:{}", list);
        List<Integer> list1 = list.subList(0, 5);
        log.info("list1:{}", list1);
    }

    @Test
    public void test28() {
        List<String> combinationResult = new ArrayList<>();
        List<String> arrangementResult = new ArrayList<>();
        String keys = "a,b,c";
        String[] keyArr = keys.split(",");
        int num = keyArr.length;
        long combinationNum = 0;
        long arrangementNum = 0;
        for (int i = 0; i < num; i++) {
            combinationNum += ACUtil.combination(num, i + 1);
            arrangementNum += ACUtil.arrangement(num, i + 1);
            combinationResult.addAll(ACUtil.combinationSelect(keyArr, i + 1));
            arrangementResult.addAll(ACUtil.arrangementSelect(keyArr, i + 1));
        }
        log.info("combinationNum:{}", combinationNum);
        log.info("combinationResult:{}", JSONObject.toJSONString(combinationResult));
        log.info("arrangementNum:{}", arrangementNum);
        log.info("arrangementResult:{}", JSONObject.toJSONString(arrangementResult));
    }

    @Test
    public void test27() {
        System.out.println("hello\nworld");
        System.out.println("============");
        System.out.println("java\rspringboot");
    }

    @Test
    public void test26() {
        System.out.println();
        BitSet bitSet1 = new BitSet(10);
        BitSet bitSet2 = new BitSet(10);
        log.info("bitSet1:{}", bitSet1);
        log.info("bitSet2:{}", bitSet2);
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                bitSet1.set(i);
            } else {
                bitSet2.set(i);
            }
        }
        log.info("set >> bitSet1:{}", bitSet1);
        log.info("set >> bitSet2:{}", bitSet2);

        BitSet bitSet1Clone1 = (BitSet) bitSet1.clone();
        log.info("clone >> bitSet1Clone1:{}", bitSet1Clone1);
        bitSet1Clone1.and(bitSet2);
        log.info("and >> bitSet1Clone1:{}", bitSet1Clone1);

        BitSet bitSet1Clone2 = (BitSet) bitSet1.clone();
        log.info("clone >> bitSet1Clone2:{}", bitSet1Clone2);
        bitSet1Clone2.or(bitSet2);
        log.info("or >> bitSet1Clone2:{}", bitSet1Clone2);

        BitSet bitSet1Clone3 = (BitSet) bitSet1.clone();
        log.info("clone >> bitSet1Clone3:{}", bitSet1Clone3);
        bitSet1Clone3.xor(bitSet2);
        log.info("xor >> bitSet1Clone3:{}", bitSet1Clone3);

        String str = "hello world";
        char charAt = str.charAt(1);
        log.info("charAt:{}", charAt);
        BitSet bitSet = new BitSet(10);
        log.info("bitSet:{}", bitSet);
        bitSet.set(charAt);
        log.info("bitSet:{}", bitSet);
    }

    @Test
    public void test24() {
        List<Integer> list = new ArrayList<>(50);
        for (int i = 0; i < 50; i++) {
            list.add(i);
        }
        Integer length = getCapacity(list);
        log.info("size:{} length:{}", list.size(), length);
    }

    public static Integer getCapacity(List<Integer> list) {
        Integer length = null;
        val clazz = list.getClass();
        Field field;
        try {
            field = clazz.getDeclaredField("elementData");
            field.setAccessible(true);
            Object[] object = (Object[]) field.get(list);
            length = object.length;
            return length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    @Test
    public void test23() {
        for (int i = 0; i < 100; i++) {
            int number = new Random().nextInt(10);
            log.info("number:{}", number);
        }
    }

    @Test
    public void test22() {
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processors:{}", processors);
    }

    @Test
    public void test21() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(Thread.currentThread().getName() + "_" + i);
        }
        List<List<String>> partition = Lists.partition(list, 10);
        log.info("partition:{}", JSONObject.toJSONString(partition));
    }

    @Test
    public void test20() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Set<Callable<Long>> callableSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            callableSet.add(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    Thread.sleep(2000);
                    long id = Thread.currentThread().getId();
                    log.info("call start!! id:{}", id);
                    return id;
                }
            });
        }
        log.info("callableSet.size:{}", callableSet.size());
        List<Future<Long>> futures = executorService.invokeAll(callableSet);
        log.info("Thread.currentThread().getId():{} futures.size:{}", Thread.currentThread().getId(), futures.size());
        List<Long> endList = new ArrayList<>();
        for (Future<Long> future : futures) {
            endList.add(future.get());
        }
        log.info("Thread.currentThread().getId():{} endList:{}", Thread.currentThread().getId(), endList);
        log.info("Thread.currentThread().getId():{} endList.size:{}", Thread.currentThread().getId(), endList.size());
    }

    @Test
    public void test19() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Object> future = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                log.info("call start!!");
                Thread.sleep(3000);
                return "call end!!";
            }
        });
        log.info("isDone:{}", future.isDone());
        Object o = future.get();
        log.info("o:{}", o);
        log.info("isDone:{}", future.isDone());
    }

    @Test
    public void test18() {
        System.out.println("main函数开始执行");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                System.out.println("===task start===");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("===task finish===");
                return 3;
            }
        }, executor);
        future.thenAccept(System.out::println);
        System.out.println("main函数执行结束");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test17() throws IOException, ExecutionException, InterruptedException {
        System.out.println("main函数开始执行");

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {

                System.out.println("===task start===");
                Thread.sleep(5000);
                System.out.println("===task finish===");
                return 3;
            }
        });
        //这里需要返回值时会阻塞主线程，如果不需要返回值使用是OK的。倒也还能接收
        Integer result = future.get();
        log.info("result:{}", result);
        System.out.println("main函数执行结束");
    }

    @Test
    public void test16() {
        System.out.println("main函数开始执行");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("===task start===");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("===task finish===");
            }
        });

        thread.start();
        System.out.println("main函数执行结束");
    }

    @Test
    public void test12() {
        UUID uuid = UUID.randomUUID();
        log.info("uuid:{}", uuid.toString());
        log.info("length:{}", uuid.toString().length());
        String abc = getSHA256StrJava("abc");
        log.info("abc:{}", abc);
    }

    /**
     * 利用java原生的摘要实现SHA256加密
     */
    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        String temp = null;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    @Test
    public void test11() {
        String str = "08bf2ad8b22ec263e9d6c9055c1591157fc6b064";
        log.info("length:{}", str.length());
    }

    @Test
    public void test10() {
        LocalDate now = LocalDate.now();
        log.info("now:{}", now.toString());
        log.info("year:{}", now.getYear());
        String dateStartStr = LocalDate.now().getYear() + "-01-01";
        java.sql.Date date = java.sql.Date.valueOf(dateStartStr);
        log.info("dateStartStr:{}", dateStartStr);
        log.info("date:{}", date);
    }

    @Test
    public void test09() {
        String str = "数据库";
        String pingYin = PinYin4JUtil.getPingYin(str);
        String pinYinHeadChar = PinYin4JUtil.getPinYinHeadChar(str);
        log.info("pingYin:{}", pingYin);
        log.info("pinYinHeadChar:{}", pinYinHeadChar);
    }

    @Test
    public void test08() {
        long timeMillis = System.currentTimeMillis();
        String format = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
        log.info("timeMillis:{}", timeMillis);
        log.info("format:{}", format);
        String week = TimeUtil.getWeek(new Date());
        log.info("week:{}", week);
    }

    @Test
    public void test07() {
        // ZoneId代表的是时区，获取系统时区：
        ZoneId zoneId = ZoneId.systemDefault();
        log.info("zoneId:{}", zoneId.toString());
        // 东八区
        ZoneId zoneIdUTC8 = ZoneId.of("UTC+8");
        log.info("zoneIdUTC8:{}", zoneIdUTC8);
        // 0时区
        LocalDate now = LocalDate.now();
        log.info("now:{}", now);
        // 东八区
        LocalDate nowUTC8 = LocalDate.now(ZoneId.of("UTC+8"));
        log.info("nowUTC8:{}", nowUTC8);
        // DateTimeFormatter 时间格式化成String
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = dateTimeFormatter.format(LocalDateTime.now(ZoneId.of("UTC+0")));
        String format2 = dateTimeFormatter.format(LocalDateTime.now(ZoneId.of("UTC+8")));
        log.info("format:{}", format);
        log.info("format2:{}", format2);
        // 时间戳
        Instant now1 = Instant.now();
        log.info("now1:{}", now1);
        now1.atZone(ZoneId.of("UTC+8"));
        log.info("now1:{}", now1);
        long l = now1.toEpochMilli();
        log.info("l:{}", l);
        // 时间戳转时间
        long timestamp = System.currentTimeMillis();
        String time = dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
        String time2 = dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC+0")));
        log.info("timestamp:{}", timestamp);
        log.info("time:{}", time);
        log.info("time2:{}", time2);
    }

    @Test
    public void test06() {
        String url = "http://127.0.0.1:8080/swagger/test";
        Map<String, String> params = new HashMap<>();
        params.put("param1", "hello");
        params.put("param2", "world");
        String response = null;
        try {
            response = HttpClientUtil.doGet(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("response:{}", response);

        String url2 = "http://127.0.0.1:8080/swagger/test2";
        String json = JSONObject.toJSONString(params);
        String response2 = null;
        try {
            response2 = HttpClientUtil.doPostJson(url2, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("response2:{}", response2);
    }

    @Test
    public void test05() {
        TestSwaggerTestResp param1 = TestSwaggerTestResp.builder().param1("param1").build();
        TestSwaggerTestResp param2 = DeepCopyUtil.deepCopy(param1);
        System.out.println(param1 == param2);
        log.info("param1:{}", JSONObject.toJSONString(param1));
        log.info("param2:{}", JSONObject.toJSONString(param2));
    }

    @Test
    public void test04() {
        String content = "test"; //明文内容
        log.info("明文:{}", content);
        System.out.println("AES加密==========");
        // AES加密
        byte[] encrypt = AESUtil.encrypt(content);
        log.info("密文:{}", encrypt);

        String parseByteToHexStr = AESUtil.parseByteToHexStr(encrypt);
        log.info("密文--字节数组转换成16进制:{}", parseByteToHexStr);
        String base64EncodeToString = AESUtil.base64EncodeToString(encrypt);
        log.info("密文--Base64处理字节数组转换为字符串:{}", base64EncodeToString);

        System.out.println("AES解密==========");
        // AES解密
        byte[] parseHexStrToByte = AESUtil.parseHexStrToByte(parseByteToHexStr);
        log.info("密文--16进制转换成字节数组:{}", parseHexStrToByte);
        byte[] base64DecodeToByte = AESUtil.base64DecodeToByte(base64EncodeToString);
        log.info("密文--Base64处理字符串转换为字节数组:{}", base64DecodeToByte);

        byte[] decryptParseHexStrToByte = AESUtil.decrypt(parseHexStrToByte);
        log.info("解密后的明文--16进制转换成字节数组:{}", decryptParseHexStrToByte);
        byte[] decryptBase64DecodeToByte = AESUtil.decrypt(base64DecodeToByte);
        log.info("解密后的明文--Base64处理字符串转换为字节数组:{}", decryptBase64DecodeToByte);
        String decryptParseHexStrToByteString = new String(decryptParseHexStrToByte, StandardCharsets.UTF_8);
        log.info("明文--字节数组转换成16进制:{}", decryptParseHexStrToByteString);
        String decryptBase64DecodeToByteString = new String(decryptBase64DecodeToByte, StandardCharsets.UTF_8);
        log.info("明文--Base64处理字节数组转换为字符串:{}", decryptBase64DecodeToByteString);
    }

    @Test
    public void test03() {
        String content = "test"; //明文内容
        log.info("明文:{}", content);
        String rsaPublicKey = RSAUtil.getRSAPublicKey();
        log.info("rsaPublicKey:{}", rsaPublicKey);
        String rsaPrivateKey = RSAUtil.getRSAPrivateKey();
        log.info("rsaPrivateKey:{}", rsaPrivateKey);
        System.out.println("RSA加密==========");
        // RSA加密
        byte[] encrypt = RSAUtil.publicEncrypt(content);
        log.info("密文:{}", encrypt);

        String parseByteToHexStr = RSAUtil.parseByteToHexStr(encrypt);
        log.info("密文--字节数组转换成16进制:{}", parseByteToHexStr);
        String base64EncodeToString = RSAUtil.base64EncodeToString(encrypt);
        log.info("密文--Base64处理字节数组转为字符串:{}", base64EncodeToString);

        System.out.println("RSA解密==========");
        // RSA解密
        byte[] parseHexStrToByte = RSAUtil.parseHexStrToByte(parseByteToHexStr);
        log.info("密文--16进制转换成字节数组:{}", parseHexStrToByte);
        byte[] base64DecodeToByte = RSAUtil.base64DecodeToByte(base64EncodeToString);
        log.info("密文--Base64处理字符串转换为字节数组:{}", base64DecodeToByte);

        byte[] decryptParseHexStrToByte = RSAUtil.privateDecrypt(parseHexStrToByte);
        log.info("解密后的明文--16进制转换成字节数组:{}", decryptParseHexStrToByte);
        byte[] decryptBase64DecodeToByte = RSAUtil.privateDecrypt(base64DecodeToByte);
        log.info("解密后的明文--Base64处理字符串转换为字节数组:{}", decryptBase64DecodeToByte);
        String decryptParseHexStrToByteString = new String(decryptParseHexStrToByte, StandardCharsets.UTF_8);
        log.info("明文--字节数组转换成16进制:{}", decryptParseHexStrToByteString);
        String decryptBase64DecodeToByteString = new String(decryptBase64DecodeToByte, StandardCharsets.UTF_8);
        log.info("明文--Base64处理字节数组转换为字符串:{}", decryptBase64DecodeToByteString);
    }

    @Test
    public void test02() {
        String username = "java";
        log.info("username:{}", username);
        Map<String, Object> map = JWTUtil.tokenGenerate(username);
        String token = String.valueOf(map.get("token"));
        String timestamp_expiration = String.valueOf(map.get("timestamp_expiration"));
        long timestamp = Long.parseLong(timestamp_expiration);
        log.info("map:{}", JSONObject.toJSONString(map));
        log.info("token:{}", token);
        log.info("timestamp_expiration:{}", timestamp_expiration);
        log.info("timestamp:{}", timestamp);
        String resolveUsername = JWTUtil.tokenResolveUsername(token);
        log.info("resolveUsername:{}", resolveUsername);
    }

    @Test
    public void test01() {
        String id = RandomUtil.generateID();
        log.info("id:{}", id);
    }
}
