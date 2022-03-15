package com.andon.springbootutil;

import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andon
 * 2022/3/15
 */
@Slf4j
public class JsoupTest {

    @Test
    public void test01() throws Exception {
        String ip = "183.129.241.197";
        Map<String, Object> ipInfo = getIpInfoFromHtml(ip);
        log.info("ipInfo:{}", JSONObject.toJSONString(ipInfo));
    }

    private Map<String, Object> getIpInfoFromHtml(String ip) throws Exception {
        Map<String, Object> ipInfo = new HashMap<>();
        String url = "https://www.hao7188.com/Home/IpInfo";
        Map<String, String> params = new HashMap<>();
        params.put("ip", ip);
        String ipInfoHtml = HttpClientUtil.doGet(url, params);
//        log.info("ipInfoHtml:{}", ipInfoHtml);
        Document document = Jsoup.parse(ipInfoHtml);
        Elements layuiCardElements = document.getElementsByClass("layui-card");
        Element layuiCardElement = layuiCardElements.get(0);
        Elements tableElements = layuiCardElement.getElementsByTag("table");
        for (Element tableElement : tableElements) {
            Elements tbodyElements = tableElement.getElementsByTag("tbody");
            for (Element tbodyElement : tbodyElements) {
                Elements trElements = tbodyElement.getElementsByTag("tr");
                Map<String, String> info = new HashMap<>();
                for (int i = 0; i < trElements.size(); i++) {
                    Element trElement = trElements.get(i);
                    Elements thElements = trElement.getElementsByTag("th");
                    if (i == 0) {
                        ipInfo.put(thElements.get(1).text(), info);
                    } else {
                        info.put(thElements.get(0).text(), thElements.get(1).text());
                    }
                }
            }
        }
        return ipInfo;
    }

    private String getIpInfo(String ip) throws Exception {
        String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php";
        Map<String, String> params = new HashMap<>();
        params.put("query", ip);
        params.put("resource_id", "6006");
        return HttpClientUtil.doGet(url, params);
    }

    private String getIpInfoFromHtml2(String ip) throws Exception {
        String url = "http://www.cip.cc" + "/" + ip;
        return HttpClientUtil.doGet(url);
    }
}
