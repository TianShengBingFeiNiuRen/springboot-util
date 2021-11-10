package com.andon.springbootutil.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Andon
 * 2021/11/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStandard<T> implements Serializable {

    private int code;
    private String message;
    private T data;
    private int total;

    public static <T> ResponseStandard<T> successResponse(T t) {
        ResponseStandard<T> response = new ResponseStandard<>();
        response.setCode(200);
        response.setMessage("success!!");
        response.setData(t);
        response.setTotal(0);
        return response;
    }

    public static <T> ResponseStandard<T> failureResponse(T t) {
        ResponseStandard<T> response = new ResponseStandard<>();
        response.setCode(-1);
        response.setMessage("failure!!");
        response.setData(t);
        response.setTotal(0);
        return response;
    }
}
