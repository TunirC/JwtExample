package com.security.jwt.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {
    HttpServletRequest request;

    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(String message, Throwable cause, HttpServletRequest request) {
        super(message, cause);
        this.request = request;
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    protected TokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
