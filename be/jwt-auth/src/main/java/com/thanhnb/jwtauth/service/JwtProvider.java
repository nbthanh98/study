package com.thanhnb.jwtauth.service;

import com.thanhnb.jwtauth.configs.SecurityProperties;
import com.thanhnb.jwtauth.models.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author thanhnb
 */
@Component
@AllArgsConstructor
public class JwtProvider {

//    @Value("${app.jwtSecret}")
//    private String jwtSecret;
//
//    @Value("${app.jwtExpirationInMs}")
//    private int jwtExpirationInMs;

    private final SecurityProperties securityProperties;

    public String genToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + securityProperties.getJwtConfigs().getJwtExpirationInMs());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, securityProperties.getJwtConfigs().getJwtSecret())
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(securityProperties.getJwtConfigs().getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(securityProperties.getJwtConfigs().getJwtSecret())
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
