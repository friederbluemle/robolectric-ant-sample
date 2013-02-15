package com.pivotallabs.api;

import java.io.InputStream;

public abstract class ApiResponse {
    protected int httpResponseCode;
    protected InputStream responseBody;

    public ApiResponse(int httpCode, InputStream responseBody) {
        this.responseBody = responseBody;
        this.httpResponseCode = httpCode;
    }

    abstract void consumeResponse() throws Exception;

    public int getResponseCode() {
        return httpResponseCode;
    }

    public boolean isSuccess() {
        return httpResponseCode >= 200 && httpResponseCode < 300;
    }

    public boolean isUnauthorized() {
        return httpResponseCode == 401;
    }
}
