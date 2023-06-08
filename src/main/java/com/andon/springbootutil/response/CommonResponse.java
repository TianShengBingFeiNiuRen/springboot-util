package com.andon.springbootutil.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Andon
 * 2023/5/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {

    private int code;
    private String message;
    private T data;
    private int total;

    public static <T> CommonResponse<T> successResponse(T t) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(0);
        response.setMessage("success!!");
        response.setData(t);
        response.setTotal(0);
        return response;
    }

    public static <T> CommonResponse<T> successResponse(T t, int total) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(0);
        response.setMessage("success!!");
        response.setData(t);
        response.setTotal(total);
        return response;
    }

    public static <T> CommonResponse<T> failureResponse(T t) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(-1);
        response.setMessage("failure!!");
        response.setData(t);
        response.setTotal(0);
        return response;
    }
}
