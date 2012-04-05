package com.pivotallabs.api;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class ApiResponse {
    private int httpResponseCode;
    private Document document;

    public ApiResponse(int httpCode, InputStream responseBody) throws IOException, SAXException, ParserConfigurationException {
        this.httpResponseCode = httpCode;
        if (isSuccess()) {
            document = Xmls.getDocument(responseBody);
        }
    }

    public int getResponseCode() {
        return httpResponseCode;
    }

    public Document getResponseDocument() {
        return document;
    }

    public boolean isSuccess() {
        return httpResponseCode >= 200 && httpResponseCode < 300;
    }

    public boolean isUnauthorized() {
        return httpResponseCode == 401;
    }
}
