package com.pivotallabs.api;

import com.pivotallabs.TestResponses;
import org.junit.Test;
import org.w3c.dom.Document;

import static com.pivotallabs.TestResponses.GENERIC_XML;
import static com.pivotallabs.api.Xmls.getTextContentOfChild;
import static com.pivotallabs.util.Strings.asStream;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class XmlApiResponseTest {

    @Test
    public void isSuccess_shouldReturnTrueIfResponseCodeIsInThe200Range() throws Exception {
        assertThat(new XmlApiResponse(200, asStream(GENERIC_XML)).isSuccess(), equalTo(true));
        assertThat(new XmlApiResponse(201, asStream(GENERIC_XML)).isSuccess(), equalTo(true));
        assertThat(new XmlApiResponse(299, asStream(GENERIC_XML)).isSuccess(), equalTo(true));
    }

    @Test
    public void isSuccess_shouldReturnFalseIfResponseCodeIsIn500Range() throws Exception {
        assertThat(new XmlApiResponse(500, asStream(GENERIC_XML)).isSuccess(), equalTo(false));
        assertThat(new XmlApiResponse(501, asStream(GENERIC_XML)).isSuccess(), equalTo(false));
    }

    @Test
    public void isUnauthorized_shouldReturnTrueIfResponseCodeIs401() throws Exception {
        assertThat(new XmlApiResponse(401, asStream("Access Denied")).isUnauthorized(), equalTo(true));
    }

    @Test
    public void parseResponse_shouldAssignAnXmlDocumentFromTheResponseBody() throws Exception {
        XmlApiResponse apiResponse = new XmlApiResponse(200, asStream(TestResponses.AUTH_SUCCESS));
        apiResponse.consumeResponse();
        Document responseDocument = apiResponse.getResponseDocument();
        assertThat(getTextContentOfChild(responseDocument, "guid"), equalTo("c93f12c"));
    }
}
