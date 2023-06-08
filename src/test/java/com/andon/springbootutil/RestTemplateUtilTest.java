package com.andon.springbootutil;

import com.andon.springbootutil.util.FileUtil;
import com.andon.springbootutil.util.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Andon
 * 2023/4/7
 */
@Slf4j
public class RestTemplateUtilTest {

    @Test
    public void test02() {
        String url = "https://10.50.2.128/_ailand/openapi/internal/_query/76983e68-2e3f-40d5-9b4a-79cf546547c5";
        String response = RestTemplateUtil.sendHttp(url, "POST", null, "[]", "JSON", null);
        log.info("response:{}", response);
    }

    @Test
    public void demo01() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8866/springboot-util/csv/download?fileName=%E5%93%88%E5%93%88&head=id%2Ckey%2Cvalue&values=1%2Ca%2Csd%7C2%2Cz%2Cxc";
        String url = "http://localhost:8866/springboot-util/csv/download?fileName=哈哈哈哈&head=id,key,value&values=1,a,sd|2,z,xc";
        ResponseEntity<byte[]> forEntity = restTemplate.getForEntity(url, byte[].class);
        byte[] body = forEntity.getBody();
        File file = FileUtil.createFile("batchParams");
        assert body != null;
//        OutputStream outputStream = new FileOutputStream(file);
//        StreamUtils.copy(body, outputStream);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(body, 0, body.length);
            outputStream.flush();
        }
        FileUtil.readFileContentByLine(file.getAbsolutePath(), (bufferedReader) -> {
            String line;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (Exception e) {
                    break;
                }
                log.info("line:{}", line);
            }
        });
        log.info("end");
    }
}
