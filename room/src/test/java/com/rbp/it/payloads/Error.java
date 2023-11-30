package com.rbp.it.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    @JsonProperty("errorCode")
    private int errorCode;

    @JsonProperty("error")
    private String error;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("fieldErrors")
    private String[] fieldErrors;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String[] getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(String[] fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}

