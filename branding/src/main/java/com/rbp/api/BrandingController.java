package com.rbp.api;

import com.rbp.model.db.Branding;
import com.rbp.model.service.BrandingResult;
import com.rbp.service.BrandingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
public class BrandingController {

    @Autowired
    private BrandingService brandingService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Branding> getBranding() throws SQLException {
        Branding branding = brandingService.getBrandingDetails();

        return ResponseEntity.ok(branding);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBranding(@Valid @RequestBody Branding branding, @CookieValue(value ="token", required = false) String token) throws SQLException {
        BrandingResult brandingResult = brandingService.updateBrandingDetails(branding, token);

        return ResponseEntity.status(brandingResult.getHttpStatus()).body(brandingResult.getBranding());
    }

}
