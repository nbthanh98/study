package com.thanhnb.jwtauth.models.jwt;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
}
