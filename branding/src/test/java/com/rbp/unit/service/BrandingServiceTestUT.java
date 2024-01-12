package com.rbp.unit.service;

import com.rbp.db.BrandingDB;
import com.rbp.model.db.Branding;
import com.rbp.model.db.Contact;
import com.rbp.model.db.Map;
import com.rbp.model.service.BrandingResult;
import com.rbp.requests.AuthRequests;
import com.rbp.service.BrandingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BrandingServiceTestUT {

    private Map sampleMap;
    private Contact sampleContact;

    @Mock
    private BrandingDB brandingDB;

    @Mock
    private AuthRequests authRequests;

    @InjectMocks
    @Autowired
    private BrandingService brandingService;

    @BeforeEach
    public void initialiseMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void initalizeBrandingAttributes(){
        sampleContact = new Contact("Demo B&B contact name", "The street", "012345", "test@email.com");
        sampleMap = new Map(2.00,4.00);
    }

    @Test
    public void queryBrandingTest() throws SQLException {
        Branding sampleBranding = new Branding("Demo B&B", sampleMap, "http://sample.url", "Branding description here", sampleContact);

        when(brandingDB.queryBranding()).thenReturn(sampleBranding);

        Branding branding = brandingService.getBrandingDetails();
        assertEquals("Branding{name='Demo B&B', map=Map{latitude=2.0, longitude=4.0}, logoUrl='http://sample.url', description='Branding description here', contact=Contact{name='Demo B&B contact name', address='The street', phone='012345', email='test@email.com'}}", branding.toString());
    }

    @Test
    public void queryBrandingFailedTest() throws SQLException{
        Branding anotherBranding = new Branding("Another B&B", sampleMap, "http://sample.url", "Branding description here", sampleContact);

        when(brandingDB.queryBranding()).thenReturn(anotherBranding);

        Branding branding = brandingService.getBrandingDetails();
        assertNotEquals("Branding{name='Demo B&B', map=Map{latitude=2.0, longitude=4.0}, logoUrl='http://sample.url', description='Branding description here', contact=Contact{name='Demo B&B contact name', address='The street', phone='012345', email='test@email.com'}}", branding.toString());
    }

    @Test
    public void updateBrandingTest() throws SQLException {
        String token = "abc";

        Branding sampleBranding = new Branding("Updated Branding", sampleMap, "http://sample.url", "Branding description here", sampleContact);

        when(brandingDB.update(sampleBranding)).thenReturn(sampleBranding);
        when(authRequests.postCheckAuth("abc")).thenReturn(true);

        BrandingResult result = brandingService.updateBrandingDetails(sampleBranding, token);

        assertEquals(HttpStatus.ACCEPTED, result.getHttpStatus());
        assertEquals("Branding{name='Updated Branding', map=Map{latitude=2.0, longitude=4.0}, logoUrl='http://sample.url', description='Branding description here', contact=Contact{name='Demo B&B contact name', address='The street', phone='012345', email='test@email.com'}}", result.getBranding().toString());
    }

    @Test
    public void updateBrandingFailedTest() throws SQLException {
        String token = "abc";

        when(authRequests.postCheckAuth(token)).thenReturn(false);

        BrandingResult result = brandingService.updateBrandingDetails(null, token);

        assertEquals(HttpStatus.FORBIDDEN, result.getHttpStatus());
    }

}
