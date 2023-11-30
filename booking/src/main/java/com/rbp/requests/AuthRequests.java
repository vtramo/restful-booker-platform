package com.rbp.requests;

import com.rbp.model.db.Token;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class AuthRequests {

    private String host;

    public AuthRequests() {
        if(System.getenv("AUTH_SERVICE_URL") == null){
            host = "http://localhost:3004";
        } else {
            host = "http://" + System.getenv("AUTH_SERVICE_URL") + ":3004";
        }
    }

    public boolean postCheckAuth(String tokenValue){
        Token token = new Token(tokenValue);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Token> httpEntity = new HttpEntity<Token>(token, requestHeaders);

        try{
            ResponseEntity<String> response = restTemplate.exchange(host + "/auth/validate", HttpMethod.POST, httpEntity, String.class);
            return response.getStatusCodeValue() == 200;
        } catch (HttpClientErrorException e){
            return false;
        }
    }

}
