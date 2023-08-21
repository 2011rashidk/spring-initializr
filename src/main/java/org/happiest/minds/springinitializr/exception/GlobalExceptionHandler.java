package org.happiest.minds.springinitializr.exception;

import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.response.SpringInitializrResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SpringInitializrResponse> globalExceptionHandler(Exception ex) {
        log.error("Exception: {}", ex.getMessage());
        SpringInitializrResponse springInitializrResponse = new SpringInitializrResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(springInitializrResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
