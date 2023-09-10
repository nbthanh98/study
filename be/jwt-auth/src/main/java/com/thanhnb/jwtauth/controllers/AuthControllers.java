package com.thanhnb.jwtauth.controllers;

import com.thanhnb.jwtauth.models.LoginDTO;
import com.thanhnb.jwtauth.service.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    @Autowired private JwtProvider jwtProvider;
    @Autowired private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginDTO login) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.genToken(authentication);
        return ResponseEntity.ok(jwt);
    }
}
