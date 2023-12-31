package com.rbp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.rbp")
public class BrandingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrandingApplication.class, args);
    }

}
