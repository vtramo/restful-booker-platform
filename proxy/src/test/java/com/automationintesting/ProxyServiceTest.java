package com.automationintesting;

import com.automationintesting.api.ProxyApplication;
import com.automationintesting.config.ConfigProperties;
import com.automationintesting.service.ProxyService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ProxyApplication.class
)
public class ProxyServiceTest {

    @Autowired
    ConfigProperties configProperties;

    ProxyService proxyService = new ProxyService(configProperties);

    @Test
    public void reportPortIsReturned(){
        int port = proxyService.derivePortNumber("/report/");
        assertEquals(3005, port);
    }

    @Test
    public void mixedPathReturnsCorrectPath(){
        int port = proxyService.derivePortNumber("/report/room");
        assertEquals(3005, port);
    }

    @Test
    public void roomPortIsReturned(){
        int port = proxyService.derivePortNumber("/room/");
        assertEquals(3001, port);
    }

    @Test
    public void assetsPortIsReturn(){
        int port = proxyService.derivePortNumber("/");
        assertEquals(3003, port);
    }

    @Test
    public void assetsPortIsReturnedForStaticAssests(){
        int port = proxyService.derivePortNumber("/js/main.js");
        assertEquals(3003, port);
    }
}
