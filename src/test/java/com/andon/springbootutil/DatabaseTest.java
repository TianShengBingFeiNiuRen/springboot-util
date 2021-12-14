package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Andon
 * 2021/12/8
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTest {

    @Resource
    private ThreadPoolExecutor globalExecutorService;
    @Resource
    private RestTemplate restTemplate;

    @Test
    public void test05() {
        Map<String, Object> param = new HashMap<>();
        param.put("param1", "hello");
        param.put("param2", "world");
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity("http://localhost:8080/aop/test3", param, JSONObject.class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        int statusCodeValue = responseEntity.getStatusCodeValue();
        HttpHeaders headers = responseEntity.getHeaders();
        JSONObject body = responseEntity.getBody();
        log.info("statusCode:{}", statusCode);
        log.info("statusCodeValue:{}", statusCodeValue);
        log.info("headers:{}", JSONObject.toJSONString(headers));
        log.info("body:{}", body);
        log.info("responseEntity:{}", JSONObject.toJSONString(responseEntity));
    }

    @Test
    public void test04() {
        Map<String, Object> param = new HashMap<>();
        param.put("value", "hello world");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/aop/test?value={value}", String.class, param);
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/aop/test?value={1}", String.class, "hello world");
        HttpStatus statusCode = responseEntity.getStatusCode();
        int statusCodeValue = responseEntity.getStatusCodeValue();
        HttpHeaders headers = responseEntity.getHeaders();
        String body = responseEntity.getBody();
        log.info("statusCode:{}", statusCode);
        log.info("statusCodeValue:{}", statusCodeValue);
        log.info("headers:{}", JSONObject.toJSONString(headers));
        log.info("body:{}", body);
        log.info("responseEntity:{}", JSONObject.toJSONString(responseEntity));
    }

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
