package com.thanhnb.jwtauth.service;

import com.thanhnb.jwtauth.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author thanhnb
 */
@Slf4j
@Component
public class CustomAuthProvider implements AuthenticationProvider {
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserDetailsService userDetailsService;
    /**
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();
        log.info("[CustomAuthProvider][authenticate] username={} Call authenticate method", username);

        // 1. find user and compare password
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String encodePassUserDB = userDetails.getPassword();
        String rawPassProvide = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(rawPassProvide, encodePassUserDB)) {
            throw new CustomException("Incorrect username or password");
        }

        // 2. Create Authentication object and setAuthenticated = true;
        return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
    }

    /**
     * AuthenticationManager call method check CustomAuthProvider support authentication flow.
     * - Return true => CustomAuthProvider support authentication flow.
     * - Return false => CustomAuthProvider not support authentication flow.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        log.info("[CustomAuthProvider][supports] Call supports method");
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
