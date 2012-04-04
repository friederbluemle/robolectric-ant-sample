package com.pivotallabs.api;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class ApiResponse {
    private int httpResponseCode;
    private InputStream responseBody;

    public ApiResponse(int httpCode, InputStream responseBody) {
        this.httpResponseCode = httpCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return httpResponseCode;
    }

    public InputStream getResponseBody() {
        return responseBody;
    }

    public Document getResponseDocument() throws IOException, SAXException, ParserConfigurationException {
        return Xmls.getDocument(getResponseBody());
    }

    public boolean isSuccess() {
        return httpResponseCode >= 200 && httpResponseCode < 300;
    }

    public boolean isUnauthorized() {
        return httpResponseCode == 401;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "httpResponseCode=" + httpResponseCode +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
