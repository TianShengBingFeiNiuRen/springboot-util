package com.andon.springbootutil.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Andon
 * 2023/6/5
 */
public class AuthorityException extends Exception {

    private final HttpStatus httpStatus;
    private final String message;

    public AuthorityException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
