package com.thanhnb.jwtauth.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "backend-security")
public class SecurityProperties {

    private Cors cors;
    private boolean enabled;
    private List<String> apiMatcher;

    @JsonProperty(value = "jwt")
    private JwtConfigs jwtConfigs;

    public CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(this.cors.getAllowedOrigins());
        corsConfiguration.setAllowedMethods(this.cors.getAllowedMethods());
        corsConfiguration.setAllowedHeaders(this.cors.getAllowedHeaders());
        corsConfiguration.setExposedHeaders(this.cors.getExposedHeaders());
        corsConfiguration.setAllowCredentials(this.cors.getAllowCredentials());
        corsConfiguration.setMaxAge(this.cors.getMaxAge());
        return corsConfiguration;
    }

    @Getter
    @Setter
    public static class Cors {
        private List<String> allowedOrigins;
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
        private List<String> exposedHeaders;
        private Boolean allowCredentials;
        private Long maxAge;
    }
    @Getter
    @Setter
    public static class JwtConfigs {
        private String jwtSecret;
        private long jwtExpirationInMs;
    }
}
