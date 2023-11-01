package com.thanhnb.jwtauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(CustomException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        @ResponseBody
        public ErrorResponse handleUnauthorizedException(CustomException ex) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMsg(ex.getMessage());
                return errorResponse;
        }
}
