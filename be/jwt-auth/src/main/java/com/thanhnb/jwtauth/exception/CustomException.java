package com.thanhnb.jwtauth.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {

    private String msg;

    public CustomException(String msg) {
        super(msg);
    }
}
