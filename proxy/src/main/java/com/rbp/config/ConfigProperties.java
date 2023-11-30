package com.rbp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "proxy")
@Validated
public class ConfigProperties {

    @NotNull
    private String authServiceUrl;

    @NotNull
    private String bookingServiceUrl;

    @NotNull
    private String brandingServiceUrl;

    @NotNull
    private String messageServiceUrl;

    @NotNull
    private String reportServiceUrl;

    @NotNull
    private String roomServiceUrl;

    @NotNull
    private String assetsServiceUrl;


    public String getAssetsServiceUrl() {
        return assetsServiceUrl;
    }

    public void setAssetsServiceUrl(String assetsServiceUrl) {
        this.assetsServiceUrl = assetsServiceUrl;
    }

    public String getAuthServiceUrl() {
        return authServiceUrl;
    }

    public String getBookingServiceUrl() {
        return bookingServiceUrl;
    }

    public String getBrandingServiceUrl() {
        return brandingServiceUrl;
    }

    public String getMessageServiceUrl() {
        return messageServiceUrl;
    }

    public String getReportServiceUrl() {
        return reportServiceUrl;
    }

    public String getRoomServiceUrl() {
        return roomServiceUrl;
    }

    public void setAuthServiceUrl(String authServiceUrl) {
        this.authServiceUrl = authServiceUrl;
    }

    public void setBookingServiceUrl(String bookingServiceUrl) {
        this.bookingServiceUrl = bookingServiceUrl;
    }

    public void setBrandingServiceUrl(String brandingServiceUrl) {
        this.brandingServiceUrl = brandingServiceUrl;
    }

    public void setMessageServiceUrl(String messageServiceUrl) {
        this.messageServiceUrl = messageServiceUrl;
    }

    public void setReportServiceUrl(String reportServiceUrl) {
        this.reportServiceUrl = reportServiceUrl;
    }

    public void setRoomServiceUrl(String roomServiceUrl) {
        this.roomServiceUrl = roomServiceUrl;
    }
}