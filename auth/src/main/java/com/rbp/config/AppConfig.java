package com.rbp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class AppConfig {

    @NotNull
    private Duration tokenLifeDuration;

    public Duration getTokenLifeDuration() {
        return tokenLifeDuration;
    }

    public void setTokenLifeDuration(Duration tokenLifeDuration) {
        this.tokenLifeDuration = tokenLifeDuration;
    }
}
