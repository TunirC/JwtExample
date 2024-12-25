package com.security.jwt.exceptions;

import com.security.jwt.model.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class HomeControllerAdvise {

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ExceptionResponse> handleTokenException(TokenException tokenException) {
        log.info("handling exception - ", tokenException);
        ExceptionResponse response = contructExceptionResponse(tokenException, HttpStatus.UNAUTHORIZED);
        response.setDescription(tokenException.getRequest().getRequestURL().toString());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        log.info("handling exception - ", badCredentialsException);
        ExceptionResponse response = contructExceptionResponse(badCredentialsException, HttpStatus.UNAUTHORIZED);
        response.setDescription(badCredentialsException.getLocalizedMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    private ExceptionResponse contructExceptionResponse(Throwable throwable, HttpStatus statusCode) {
        return ExceptionResponse.builder()
                .statusCode(statusCode)
                .timestamp(new Date())
                .message(throwable.getMessage())
                .description("").build();
    }


}
