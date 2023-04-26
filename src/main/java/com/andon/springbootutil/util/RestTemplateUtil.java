package com.andon.springbootutil.util;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andon
 * 2023/3/9
 */
@Slf4j
public class RestTemplateUtil {

    private static RestTemplate restTemplate;
    private static final int CONNECT_TIMEOUT = 60 * 1000;
    private static final int READ_TIMEOUT = 60 * 1000;

    static {
        HttpClientHttpRequestFactory requestFactory = new HttpClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
    }

    @SuppressWarnings("NullableProblems")
    static class HttpClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
        @SneakyThrows
        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
            if (connection instanceof HttpsURLConnection) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                // 信任任何链接忽略对证书的校验
                TrustStrategy anyTrustStrategy = (x509Certificates, s) -> true;
                // 自定义SSLContext
                SSLContext ctx = SSLContexts.custom().loadTrustMaterial(trustStore, anyTrustStrategy).build();
                // SSL问题
                ((HttpsURLConnection) connection).setSSLSocketFactory(ctx.getSocketFactory());
                // 解决No subject alternative names matching IP address xxx.xxx.xxx.xxx found问题
                ((HttpsURLConnection) connection).setHostnameVerifier((s, sslSession) -> true);
                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                super.prepareConnection(httpsConnection, httpMethod);
            } else {
                super.prepareConnection(connection, httpMethod);
            }
        }
    }

    public static String sendHttp(String url, String method, Map<String, ?> params, String body, String bodyType, Map<String, ?> headers) {
        // url拼接params参数
        url = buildUrlParams(url, params);
        // 请求方式
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        // 构建请求头
        HttpHeaders httpHeaders = buildHttpHeaders(headers, bodyType);
        // 构建请求体
        HttpEntity<?> httpEntity = buildHttpEntity(httpHeaders, body);
        // 发送请求返回响应
        log.info("url:{} httpMethod:{} httpHeaders:{} httpEntity:{}", url, httpMethod, JSONObject.toJSONString(httpHeaders), JSONObject.toJSONString(httpEntity));
        return restTemplate.exchange(url, httpMethod, httpEntity, String.class).getBody();
    }

    /**
     * url拼接params参数
     */
    private static String buildUrlParams(String url, Map<String, ?> params) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        url = url.split("\\?")[0];
        if (ObjectUtils.isEmpty(params)) {
            return url;
        }
        StringBuilder paramsBuilder = new StringBuilder(url);
        paramsBuilder.append("?");
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            paramsBuilder.append(entry.getKey());
            paramsBuilder.append("=");
            paramsBuilder.append(entry.getValue());
            paramsBuilder.append("&");
        }
        paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
        return paramsBuilder.toString();
    }

    /**
     * 构建请求头
     */
    private static HttpHeaders buildHttpHeaders(Map<String, ?> headers, String bodyType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (ObjectUtils.nullSafeEquals(bodyType, BodyTypeEnum.FORM_DATA.toString())) {
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        } else if (ObjectUtils.nullSafeEquals(bodyType, BodyTypeEnum.JSON.toString())) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        if (!ObjectUtils.isEmpty(headers)) {
            for (Map.Entry<String, ?> entry : headers.entrySet()) {
                httpHeaders.set(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return httpHeaders;
    }

    /**
     * 构建请求体
     */
    private static HttpEntity<?> buildHttpEntity(HttpHeaders httpHeaders, String body) {
        if (ObjectUtils.isEmpty(body)) {
            body = "{}";
        }
        if (!ObjectUtils.isEmpty(httpHeaders) && !ObjectUtils.isEmpty(httpHeaders.getContentType())) {
            if (ObjectUtils.nullSafeEquals(httpHeaders.getContentType(), MediaType.MULTIPART_FORM_DATA)) {
                MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
                for (Map.Entry<String, Object> entry : JSONObject.parseObject(body).entrySet()) {
                    formData.add(entry.getKey(), entry.getValue());
                }
                return new HttpEntity<>(formData, httpHeaders);
            } else if (ObjectUtils.nullSafeEquals(httpHeaders.getContentType(), MediaType.APPLICATION_JSON)) {
                return new HttpEntity<>(body, httpHeaders);
            }
        }
        return new HttpEntity<>(body, httpHeaders);
    }

    enum BodyTypeEnum {
        NONE,
        FORM_DATA,
        JSON
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        {
            String url = "127.0.0.1:8866/springboot-util/swagger/test?param1=hello&param2=world";
            String method = "GET";
            Map<String, Object> params = new HashMap<>();
            params.put("param1", "hello");
            params.put("param2", "world");
            String response = RestTemplateUtil.sendHttp(url, method, params, null, "NONE", null);
            log.info("response:{}", response);
        }
        System.out.println();
        {
            String url = "127.0.0.1:8866/springboot-util/swagger/test2";
            String method = "POST";
            Map<String, Object> body = new HashMap<>();
            body.put("param1", "java");
            body.put("param2", "mysql");
            String response = RestTemplateUtil.sendHttp(url, method, null, JSONObject.toJSONString(body), "JSON", null);
            log.info("response:{}", response);
        }
    }
}
