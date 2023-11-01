package com.automationintesting.api;

import com.automationintesting.config.AppConfig;
import com.automationintesting.config.DatabaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.automationintesting", "com.automationintesting.config"})
@EnableConfigurationProperties({DatabaseConfig.class, AppConfig.class})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
