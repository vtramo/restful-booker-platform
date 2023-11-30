package com.rbp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class AppConfig {

    @NotNull
    private String authServiceClientUrl;

    public String getAuthServiceClientUrl() {
        return authServiceClientUrl;
    }

    public void setAuthServiceClientUrl(String authServiceUrl) {
        this.authServiceClientUrl = authServiceUrl;
    }
}
