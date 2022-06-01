package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.dto.RocksDBVo;
import com.andon.springbootutil.dto.TestSwaggerTestResp;
import com.andon.springbootutil.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
public class Demo {

    @Test
    public void test54() {
        List<String> list = Arrays.asList("hello", "world", "java", "hello", "springboot");
        Map<String, String> map = list.stream().collect(Collectors.toMap(s -> s, s -> s, (s, s2) -> s));
        log.info("map:{}", JSONObject.toJSONString(map));
    }

    @Test
    public void test53() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(String.valueOf(i));
        }
        List<String> subList = list.subList(0, Math.min(list.size(), 3));
        log.info("subList:{}", JSONObject.toJSONString(subList));
    }

    @Test
    public void test52() {
        for (int i = 1; i <= 10; i++) {
            String stringGenerate = RandomStringUtil.stringGenerate(i, true, false, false);
            log.info("i={} -> stringGenerate:{} length:{}", i, stringGenerate, stringGenerate.length());
        }
    }

    @Test
    public void test51() {
        String str = "hello";
        String substring = str.substring(0, 3);
        log.info("substring:{}", substring);
    }

    @Test
    public void test50() {
        Integer[] integers = new Integer[]{2, 3};
        boolean allMatch = Arrays.stream(integers).allMatch(integer -> integer % 2 == 0);
        boolean anyMatch = Arrays.stream(integers).anyMatch(integer -> integer % 2 == 0);
        log.info("allMatch:{}", allMatch);
        log.info("anyMatch:{}", anyMatch);
    }

    @Test
    public void test49() {
        long millis = TimeUnit.MINUTES.toMillis(1);
        log.info("millis:{}", millis);
    }

    @Test
    public void test48() {
        double dd = 0.345365345;
        DecimalFormat decimalFormat = new DecimalFormat("#.##%");
        String format = decimalFormat.format(dd);
        log.info("format:{}", format);
    }

    @Test
    public void test47() {
        String string = getString();
        assert string != null;
        log.info("string:{}", string);
    }

    private String getString() {
        return null;
    }

    @Test
    public void test46() {
        String[] strings = new String[]{"hello"};
        validate(strings);
        log.info("strings:{}", JSONObject.toJSONString(strings));
    }

    private void validate(String[] strings) {
        Assert.isTrue(strings.length > 1, "元素个数至少需要2个!!");
    }

    @Test
    public void test45() {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("springboot", "java");
        concurrentHashMap.put("mysql", "database");
        log.info("concurrentHashMap:{}", JSONObject.toJSONString(concurrentHashMap));
        concurrentHashMap.remove("mysql");
        log.info("concurrentHashMap:{}", JSONObject.toJSONString(concurrentHashMap));
    }

    @Test
    public void test44() throws Exception {
        String ip = "113.225.172.225";
        Map<String, String> param = new HashMap<>();
        param.put("query", ip);
        param.put("resource_id", "6006");
        String url_remote_address_info = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php";
        String response = HttpClientUtil.doGet(url_remote_address_info, param);
        log.info("response:{}", response);
    }

    @Test
    public void test43() {
        String str = "223 3 #d 的 是个很__  EEE=+\uD83C\uDF26";
        log.info("str:{}", str);
        String str2 = str.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
        log.info("str2:{}", str2);
    }

    @Test
    public void test42() {
        String[] strings = new String[]{"hello", "world", "java", "python"};
        List<String> collect = Arrays.stream(strings)
                .filter(s -> {
                    return !s.contains("o");
                })
                .collect(Collectors.toList());
        log.info("collect:{}", JSONObject.toJSONString(collect));
    }

    @Test
    public void test41() {
        String tableName = "history_price_600145_xshg";
        String s = "history_price_";
        String substring = tableName.substring(s.length());
        log.info("substring:{}", substring);
    }

    @Test
    public void test40() throws IOException {
        String apiHostPort = "https://61.164.47.197:3443/blockchain";
        log.info("apiHostPort:{}", apiHostPort);
        String userRegisterUrl = "/api/v1/user/register";
        String headerAccessKey = "AuthCode";
        String headerAccessCode = "test";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", "17610835229");
        String json = JSONObject.toJSONString(jsonObject);
        Map<String, String> access = new HashMap<>();
        access.put(headerAccessKey, headerAccessCode);
        String response = HttpClientUtil.doPostJson(apiHostPort + userRegisterUrl, access, json);
        log.info("response:{}", response);
    }

    @Test
    public void test39() {
        RocksDBVo rocksDBVo = RocksDBVo.builder().cfName("cfName").key("key").value("value").build();
        log.info("rocksDBVo:{}", JSONObject.toJSONString(rocksDBVo));
        Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(rocksDBVo), new com.alibaba.fastjson.TypeReference<Map<String, Object>>() {
        }.getType());
        log.info("map:{}", JSONObject.toJSONString(map));
    }

    @Test
    public void test38() {
        long ll = 9223372036854775807L;
        long year = ll / 60 / 60 / 24 / 365;
        log.info("{}", year);
    }

    @Test
    public void test37() {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        log.info(s);
    }

    @Test
    public void test36() {
        List<String[]> result = new ArrayList<>();
        String[] strArr = new String[]{"a,s,d", "z,x,c", "-1", "1,2,3", "4,5,6", "-1", "p,o,i"};
        Arrays.stream(strArr)
                .filter(value -> !value.equals("-1"))
                .forEach(value -> result.add(value.split(",")));
        log.info("result:{}", JSONObject.toJSONString(result));
    }

    @Test
    public void test35() {
        Optional<Schema> schemaOptional = Optional.empty();
        Schema schema = schemaOptional.orElse(null);
        log.info("schema:{}", JSONObject.toJSONString(schema));

        schemaOptional = Optional.ofNullable(Schema.builder().isParticipateCompute(true).fieldName("a").dataType(DataType.STRING).build());
        schema = schemaOptional.orElse(null);
        log.info("schema:{}", JSONObject.toJSONString(schema));
    }

    @Test
    public void test34() {
        Schema schema1 = Schema.builder().isParticipateCompute(true).fieldName("a").dataType(DataType.STRING).build();
        Schema schema2 = Schema.builder().isParticipateCompute(false).fieldName("b").dataType(DataType.STRING).build();
        Schema schema3 = Schema.builder().isParticipateCompute(true).fieldName("c").dataType(DataType.STRING).build();
        Schema schema4 = Schema.builder().isParticipateCompute(true).fieldName("d").dataType(DataType.STRING).build();
        List<Schema> list = new ArrayList<>();
        list.add(schema1);
        list.add(schema2);
        list.add(schema3);
        list.add(schema4);
        log.info("list:{}", list);

        String json = new String(SerializationUtil.serialize(list));
        log.info("json:{}", json);
        List<Schema> schemas = SerializationUtil.deserialize(json.getBytes(), new TypeReference<List<Schema>>() {
        });
        log.info("schemas:{}", JSONObject.toJSONString(schemas));
        List<String> indexs = new ArrayList<>();
        for (int i = 0; i < schemas.size(); i++) {
            if (schemas.get(i).isParticipateCompute) {
                indexs.add(String.valueOf(i));
            }
        }
        log.info("indexs:{}", indexs);
        long combinationNum = 0;
        List<String> combinationIndexs = new ArrayList<>();
        for (int i = 0; i < indexs.size(); i++) {
            combinationNum += ACUtil.combination(indexs.size(), i + 1);
            combinationIndexs.addAll(ACUtil.combinationSelect(indexs.toArray(new String[0]), i + 1));
        }
        log.info("combinationIndex:{}", JSONObject.toJSONString(combinationIndexs));
        for (String combinationIndex : combinationIndexs) {
            String[] indexArr = combinationIndex.split(",");
            Optional<String> fieldNameOptional = Arrays.stream(indexArr)
                    .map(index -> schemas.get(Integer.parseInt(index)).getFieldName())
                    .reduce((s, s2) -> s + "," + s2);
//            fieldNameOptional.ifPresent(fieldName -> log.info("combinationIndex:{} fieldName:{}", combinationIndex, fieldName));
            List<String> fieldNames = new ArrayList<>();
            for (int i = 0; i < indexArr.length; i++) {
                fieldNames.add(schemas.get(i).getFieldName());
            }
            String fieldName = String.join(",", fieldNames);
            log.info("combinationIndex:{} fieldName:{}", combinationIndex, fieldName);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Schema {
        private Boolean isParticipateCompute; //是否参与计算
        private String fieldName; //字段名
        private DataType dataType; //数据类型
        private String description; //字段描述
    }

    private enum DataType {
        STRING, INT, FLOAT, DOUBLE, DECIMAL, TIMESTAMP, BOOLEAN
    }

    static class SerializationUtil {
        private final static ObjectMapper mapper = new ObjectMapper();

        static {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        private SerializationUtil() {
        }

        /**
         * 序列化(对象 -> 字节数组)
         *
         * @param obj 对象
         * @return 字节数组
         */
        public static <T> byte[] serialize(T obj) {
            try {
                return mapper.writeValueAsBytes(obj);
            } catch (JsonProcessingException e) {
                log.error("序列化失败: ", e);
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        /**
         * 反序列化(字节数组 -> 对象)
         *
         * @param data
         * @param valueTypeRef
         * @param <T>
         */
        public static <T> T deserialize(byte[] data, TypeReference<T> valueTypeRef) {
            try {
                return mapper.readValue(data, valueTypeRef);
            } catch (IOException e) {
                log.error("反序列化失败: ", e);
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        /**
         * 反序列化(字节数组 -> 对象)
         *
         * @param data
         * @param cls
         * @param <T>
         */
        public static <T> T deserialize(byte[] data, Class<T> cls) {
            try {
                return mapper.readValue(data, cls);
            } catch (IOException e) {
                log.error("反序列化失败: ", e);
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        /**
         * 反序列化(字节数组 -> 对象)
         *
         * @param jsonStr
         * @param cls
         * @param <T>
         */
        public static <T> T deserialize(String jsonStr, Class<T> cls) {
            try {
                return mapper.readValue(jsonStr, cls);
            } catch (IOException e) {
                log.error("反序列化失败: ", e);
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    @Test
    public void test33() {
        RocksDBVo build = RocksDBVo.builder().cfName("cfName").key("key").value("value").build();
        log.info("build:{}", JSONObject.toJSONString(build));
        RocksDBVo rocksDBVo = RocksDBVo.builder().cfName("cfName").value("阿萨德阿萨德").build();
        log.info("rocksDBVo:{}", JSONObject.toJSONString(rocksDBVo));
        BeanUtils.copyProperties(rocksDBVo, build);
        log.info("build:{}", JSONObject.toJSONString(build));
    }

    @Test
    public void test32() {
        String str = "";
        str = getStr();
        switch (str) {
            case "hello":
                str = "hello";
                break;
            case "world":
                str = "world";
                break;
            case "java":
                str = "java";
                break;
        }
        log.info(str);
    }

    private String getStr() {
        return "hello";
    }

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
//        String keys = "a,b,c";
//        String[] keyArr = keys.split(",");
        int length = 20;
        String[] keyArr = new String[length];
        for (int i = 0; i < length; i++) {
            keyArr[i] = String.valueOf(i);
        }
        int num = keyArr.length;
        long combinationNum = 0;
        long arrangementNum = 0;
        for (int i = 0; i < num; i++) {
            combinationNum += ACUtil.combination(num, i + 1);
//            arrangementNum += ACUtil.arrangement(num, i + 1);
            combinationResult.addAll(ACUtil.combinationSelect(keyArr, i + 1));
//            arrangementResult.addAll(ACUtil.arrangementSelect(keyArr, i + 1));
        }
        log.info("combinationNum:{}", combinationNum);
//        log.info("combinationResult:{}", JSONObject.toJSONString(combinationResult));
//        log.info("arrangementNum:{}", arrangementNum);
//        log.info("arrangementResult:{}", JSONObject.toJSONString(arrangementResult));
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
    public void test18() throws InterruptedException {
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
        Thread.sleep(10000);
    }

    @Test
    public void test17() throws ExecutionException, InterruptedException {
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
    public void test16() throws InterruptedException {
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
//        Thread.sleep(10000);
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
        String id = RandomStringUtil.generateID();
        log.info("id:{}", id);
    }
}
