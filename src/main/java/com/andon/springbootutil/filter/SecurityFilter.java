package com.andon.springbootutil.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andon.springbootutil.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
//@WebFilter(urlPatterns = "/*", filterName = "securityFilter")
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String location = queryLocation(httpServletRequest);
        Map<String, Object> ipInfo = getIpInfoFromHtml(httpServletRequest);
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(httpServletRequest);
        String parameterJson = getParameterJson(requestWrapper);
        log.info("X-Real-IP:{} location:{} ipInfo:{} remoteHost:{} method:{} uri:{} parameterMap:{} parameterJson:{}", httpServletRequest.getHeader("X-Real-IP"), location, JSONObject.toJSONString(ipInfo), httpServletRequest.getRemoteHost(), httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), JSONObject.toJSONString(parameterMap), parameterJson);
        if (httpServletRequest.getRequestURI().equals("/springboot-util/filter")) {
            httpServletRequest.setAttribute("code", 403);
            httpServletRequest.setAttribute("message", "没有权限!!");
            httpServletRequest.setAttribute("uri", httpServletRequest.getRequestURI());
            // 请求转发到自定义403接口处理响应
            httpServletRequest.getRequestDispatcher("/403").forward(requestWrapper, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    public static String queryLocation(HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-Real-IP");
        Optional<String> ipOptional = Optional.ofNullable(ip);
        ip = ipOptional.orElse(httpServletRequest.getRemoteHost());
        String location = ip;
        try {
            Map<String, String> param = new HashMap<>();
            param.put("query", ip);
            param.put("resource_id", "6006");
            String response = HttpClientUtil.doGet("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php", param);
            if (!ObjectUtils.isEmpty(response)) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray data = jsonObject.getJSONArray("data");
                if (!ObjectUtils.isEmpty(data)) {
                    JSONObject dataJSONObject = data.getJSONObject(0);
                    location = dataJSONObject.getString("location");
                }
            }
        } catch (Exception e) {
            log.error("queryLocation failure!! ip:{} error:{}", ip, e.getMessage());
        }
        return location;
    }

    public static Map<String, Object> getIpInfoFromHtml(HttpServletRequest httpServletRequest) {
        Map<String, Object> ipInfo = new HashMap<>();
        String ip = httpServletRequest.getHeader("X-Real-IP");
        Optional<String> ipOptional = Optional.ofNullable(ip);
        ip = ipOptional.orElse(httpServletRequest.getRemoteHost());
        try {
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
        } catch (Exception e) {
            log.error("getIpInfoFromHtml failure!! ip:{} error:{}", ip, e.getMessage());
        }
        return ipInfo;
    }

    public static String getParameterJson(CustomHttpServletRequestWrapper requestWrapper) {
        byte[] body = requestWrapper.getBody();
        String parameterJson = new String(body);
        parameterJson = parameterJson.equals("") ? "{}" : parameterJson.replaceAll("\\s", "").replaceAll("\n", "");
        return parameterJson;
    }
}
