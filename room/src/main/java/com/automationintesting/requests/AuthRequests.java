package com.automationintesting.requests;

import com.automationintesting.config.AppConfig;
import com.automationintesting.model.request.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class AuthRequests {

    private final AppConfig appConfig;

    @Autowired
    public AuthRequests(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    private String url() {
        return appConfig.getAuthServiceClientUrl();
    }

    public boolean postCheckAuth(String tokenValue) {
        Token token = new Token(tokenValue);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Token> httpEntity = new HttpEntity<>(token, requestHeaders);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url() + "/auth/validate", HttpMethod.POST, httpEntity, String.class);
            return response.getStatusCodeValue() == 200;
        } catch (HttpClientErrorException e) {
            return false;
        }
    }

}
