package com.andon.springbootutil.controller;

import com.andon.springbootutil.domain.ResponseStandard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andon
 * 2021/11/10
 */
@RestController
public class FilterController {

    /**
     * 自定义403接口处理响应
     */
    @RequestMapping(value = "/403")
    public ResponseStandard<String> filter(HttpServletRequest request) {
        ResponseStandard<String> response = ResponseStandard.failureResponse(null);
        try {
            int code = (int) request.getAttribute("code");
            String message = String.valueOf(request.getAttribute("message"));
            response.setCode(code);
            response.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping(value = "/filter")
    public void filter() {

    }
}
