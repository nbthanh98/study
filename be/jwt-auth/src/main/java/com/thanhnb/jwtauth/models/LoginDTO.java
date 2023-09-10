package com.thanhnb.jwtauth.models;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
}
