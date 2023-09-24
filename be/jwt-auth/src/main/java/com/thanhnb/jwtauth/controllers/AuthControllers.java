package com.thanhnb.jwtauth.controllers;

import com.thanhnb.jwtauth.models.jwt.LoginDTO;
import com.thanhnb.jwtauth.service.JwtProvider;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    @PostMapping("/signin-custom")
    public ResponseEntity<Object> authWithCustomAuthProvider(@RequestBody LoginDTO login) {
        log.info("[AuthControllers][authWithCustomAuthProvider] username={} Call authWithCustomAuthProvider", login.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.genToken(authentication);
        log.info("[AuthControllers][authWithCustomAuthProvider] username={} login success", login.getUsername());
        return ResponseEntity.ok(jwt);
    }
}
