package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.FileUtil;
import com.andon.springbootutil.util.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Andon
 * 2024/12/17
 */
@Slf4j
public class CollectorJobTest {

    @Test
    public void demo() throws Exception {
        String templateJsonPath = "C:\\IdeaProjects\\springboot-util\\src\\test\\java\\com\\andon\\springbootutil\\http-template.json";
        String templateJson = FileUtil.readContent(templateJsonPath);

        JSONObject jobParams = JSONObject.parseObject(templateJson);
        jobParams.getJSONObject("job").put("callbackUrl", "http://127.0.0.1:8866/callback");
        JSONObject readerParameter = jobParams.getJSONObject("job").getJSONArray("content").getJSONObject(0).getJSONObject("reader").getJSONObject("parameter");
        readerParameter.put("url", "http://10.50.2.166:18886/third/data-route/compliance-scene-info");
        readerParameter.put("method", "GET");
        readerParameter.put("params", "[\n" +
                "            {\n" +
                "                \"key\": \"sceneId\",\n" +
                "                \"value\": \"hgjfcj0000100200026\",\n" +
                "                \"dataType\": \"STRING\",\n" +
                "                \"desc\": \"\"\n" +
                "            }\n" +
                "        ]");
        readerParameter.put("bodyType", "NONE");
        readerParameter.put("headers", "[]");
        readerParameter.put("response", "[\n" +
                "            {\n" +
                "                \"fieldName\": \"id\",\n" +
                "                \"dataType\": \"STRING\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"fieldName\": \"name\",\n" +
                "                \"dataType\": \"STRING\"\n" +
                "            }\n" +
                "        ]");
        readerParameter.put("dataPath", "[\n" +
                "            {\n" +
                "                \"dataPath\": \"根节点\",\n" +
                "                \"dataType\": \"OBJECT\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"dataPath\": \"data\",\n" +
                "                \"dataType\": \"OBJECT\"\n" +
                "            }\n" +
                "        ]");
        readerParameter.put("extend", "{\"isBatchParams\":false}");

        long timestamp = System.currentTimeMillis();
        jobParams.getJSONObject("core").getJSONObject("container").getJSONObject("job").put("id", "jobId-" + timestamp);
        JSONObject writer = jobParams.getJSONObject("job").getJSONArray("content").getJSONObject(0).getJSONObject("writer");
        writer.put("jobId", "jobId-" + timestamp);
        writer.put("jobVersion", timestamp);
        JSONObject writerParameter = writer.getJSONObject("parameter");
        writerParameter.put("version", timestamp);
        writerParameter.put("fullFileName", "jobId-" + timestamp + ".csv");
        writerParameter.put("fullFilePath", "/data/apps/data-route-endpoint/package/ailand-data-collector-executor2/temp/" + "jobId-" + timestamp + ".csv");
        writerParameter.put("fileName", "jobId-" + timestamp + ".csv");
        writerParameter.put("path", "/data/apps/data-route-endpoint/package/ailand-data-collector-executor2/temp/" + "jobId-" + timestamp + ".csv");
        writerParameter.put("writeMode", "truncate");

        JSONObject body = new JSONObject();
        body.put("jobContext", JSONObject.toJSONString(jobParams));
        log.info("body:{}", JSONObject.toJSONString(body));
        String response = RestTemplateUtil.sendHttp("http://10.50.3.141:6116/api/collector/async/job", "POST", null, JSONObject.toJSONString(body), "JSON", null);
    }
}
