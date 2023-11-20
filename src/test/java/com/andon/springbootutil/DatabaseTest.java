package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.config.properties.AndonAddConfig;
import com.andon.springbootutil.config.properties.IpWhiteListProperties;
import com.andon.springbootutil.entity.AuthorityUser;
import com.andon.springbootutil.mapper.DatabaseMapper;
import com.andon.springbootutil.util.FileUtil;
import com.andon.springbootutil.util.PinYin4JUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andon
 * 2021/12/8
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTest {

    @Value("${env}")
    private String env;
    @Resource
    private ThreadPoolExecutor globalExecutorService;
    //    @Resource
//    private RestTemplate restTemplate;
    @Resource
    private AndonAddConfig andonAddConfig;
    @Resource
    private IpWhiteListProperties ipWhiteListProperties;
    @Resource
    private DatabaseMapper databaseMapper;

    @Test
    public void test09() throws Exception {
        File file = new File("C:\\DingDing Files\\parking有表头.csv");
        String firstLine = FileUtil.readFirstLine(file.getAbsolutePath());
        List<String> schema = Arrays.asList(firstLine.split(","));
        log.info("schema:{}", JSONObject.toJSONString(schema));
        String table = file.getName().split("\\.")[0];

        String tableName = PinYin4JUtil.getPinYinHeadChar(table).replaceAll(".*[./*~!@#$%^&?\\-_=+\\[\\]{}].*", "") + "_" + System.currentTimeMillis();
        databaseMapper.dropTable(tableName);
        databaseMapper.createTable(tableName, table);
        for (String column : schema) {
            databaseMapper.addColumn(tableName, column);
        }
        AtomicInteger row = new AtomicInteger();
        String column = String.format("`%s`", String.join("`,`", schema));
        int batchSize = 1000;
        List<String> lines = new ArrayList<>(batchSize);
        FileUtil.readFileContentByLine(file.getAbsolutePath(), (bufferedReader) -> {
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
                        databaseMapper.insertInto(tableName, column, stringBuilder.toString());
                        row.addAndGet(lines.size());
                        log.info("tableName:{} row:{}", tableName, row.get());
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
                databaseMapper.insertInto(tableName, column, stringBuilder.toString());
                row.addAndGet(lines.size());
                log.info("tableName:{} row:{}", tableName, row.get());
            }
        });
    }

    @Test
    public void test08() {
        List<AuthorityUser> authorityUsers = databaseMapper.selectUser();
        log.info("authorityUsers:{}", JSONObject.toJSONString(authorityUsers));
    }

    @Test
    public void test07() {
        List<String> ips = ipWhiteListProperties.getIps();
        log.info("{}", JSONObject.toJSONString(ips));
    }

    @Test
    public void test06() {
        log.info("env:{}", env);
        log.info("andonAddProperties.getEnable()={} andonAddProperties.getName()={}", andonAddConfig.getEnable(), andonAddConfig.getName());
    }

//    @Test
//    public void test05() {
//        Map<String, Object> param = new HashMap<>();
//        param.put("param1", "hello");
//        param.put("param2", "world");
//        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity("http://localhost:8080/aop/test3", param, JSONObject.class);
//        HttpStatus statusCode = responseEntity.getStatusCode();
//        int statusCodeValue = responseEntity.getStatusCodeValue();
//        HttpHeaders headers = responseEntity.getHeaders();
//        JSONObject body = responseEntity.getBody();
//        log.info("statusCode:{}", statusCode);
//        log.info("statusCodeValue:{}", statusCodeValue);
//        log.info("headers:{}", JSONObject.toJSONString(headers));
//        log.info("body:{}", body);
//        log.info("responseEntity:{}", JSONObject.toJSONString(responseEntity));
//    }

//    @Test
//    public void test04() {
//        Map<String, Object> param = new HashMap<>();
//        param.put("value", "hello world");
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/aop/test?value={value}", String.class, param);
////        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/aop/test?value={1}", String.class, "hello world");
//        HttpStatus statusCode = responseEntity.getStatusCode();
//        int statusCodeValue = responseEntity.getStatusCodeValue();
//        HttpHeaders headers = responseEntity.getHeaders();
//        String body = responseEntity.getBody();
//        log.info("statusCode:{}", statusCode);
//        log.info("statusCodeValue:{}", statusCodeValue);
//        log.info("headers:{}", JSONObject.toJSONString(headers));
//        log.info("body:{}", body);
//        log.info("responseEntity:{}", JSONObject.toJSONString(responseEntity));
//    }

    @Test
    public void test03() throws InterruptedException, ExecutionException {
        Set<Callable<Long>> callableSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            callableSet.add(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    long id = Thread.currentThread().getId();
                    if (id % 2 == 0) {
                        log.warn("return!! id:{}", id);
                        return (long) -1;
                    }
                    Thread.sleep(2000);
                    log.info("call start!! id:{}", id);
                    return id;
                }
            });
        }
        log.info("callableSet.size:{}", callableSet.size());
        List<Future<Long>> futures = globalExecutorService.invokeAll(callableSet);
        log.info("Thread.currentThread().getId():{} futures.size:{}", Thread.currentThread().getId(), futures.size());
        List<Long> endList = new ArrayList<>();
        for (Future<Long> future : futures) {
            endList.add(future.get());
        }
        log.info("Thread.currentThread().getId():{} endList:{}", Thread.currentThread().getId(), endList);
        log.info("Thread.currentThread().getId():{} endList.size:{}", Thread.currentThread().getId(), endList.size());
    }

    @Test
    public void test02() throws ExecutionException, InterruptedException {
        log.info("globalExecutorService:{}", globalExecutorService);
        Future<Object> future = globalExecutorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                log.info("Thread.currentThread() >> id:{} name:{} call start!!", Thread.currentThread().getId(), Thread.currentThread().getName());
                Thread.sleep(3000);
                return "call end!!";
            }
        });
        log.info("Thread.currentThread() >> id:{} name:{} isDone:{}", Thread.currentThread().getId(), Thread.currentThread().getName(), future.isDone());
        Object o = future.get();
        log.info("o:{}", o);
        log.info("Thread.currentThread() >> id:{} name:{} isDone:{}", Thread.currentThread().getId(), Thread.currentThread().getName(), future.isDone());
    }

    @Test
    public void test01() {
        log.info("test01!!");
    }
}
