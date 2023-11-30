package com.rbp.it.requests;

public abstract class BaseApi {
    private final String url;

    public BaseApi(String url) {
        this.url = url.endsWith("/") ? url : url + "/";
    }

    public String url() {
        return url;
    }
}
