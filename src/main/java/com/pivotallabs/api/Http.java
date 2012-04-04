package com.pivotallabs.api;

import android.content.Context;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.Map;

import static com.pivotallabs.util.Strings.isEmptyOrWhitespace;

public class Http {

    private Context context;
    private int[] certStoreResIds;

    public Http(Context context, int... certStoreResIds) {
        this.context = context;
        this.certStoreResIds = certStoreResIds;
    }

    public Response get(String url, Map<String, String> headers, String username, String password)
            throws IOException, URISyntaxException {
        URI uri = new URI(url);
        return makeRequest(headers, username, password, new HttpGet(uri), uri.getHost());
    }

    public Response post(String url, Map<String, String> headers, String postBody, String username, String password)
            throws IOException, URISyntaxException {
        URI uri = new URI(url);
        HttpPost method = new HttpPost(uri);
        method.setEntity(new StringEntity(postBody, "UTF-8"));
        return makeRequest(headers, username, password, method, uri.getHost());
    }

    private Response makeRequest(Map<String, String> headers, String username, String password, HttpRequestBase method, String host) {
        DefaultHttpClient client = null;
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                method.setHeader(entry.getKey(), entry.getValue());
            }
            client = getHttpClient();
            addBasicAuthCredentials(client, host, username, password);
            return new Response(client.execute(method));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                try {
                    client.getConnectionManager().shutdown();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void addBasicAuthCredentials(DefaultHttpClient client, String domainName, String username, String password) {
        if (!isEmptyOrWhitespace(username) || !isEmptyOrWhitespace(password)) {
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            client.getCredentialsProvider().setCredentials(new AuthScope(domainName, 443), credentials);
        }
    }

    private DefaultHttpClient getHttpClient() {
        HttpParams parameters = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", createSSLSocketFactory(), 443));
        return new DefaultHttpClient(new SingleClientConnManager(parameters, schemeRegistry), parameters);
    }

    private SocketFactory createSSLSocketFactory() {
        try {
            KeyStore trusted = KeyStore.getInstance(KeyStore.getDefaultType());
            for (int certStoreResId : certStoreResIds) {
                // Bob explains how to create the cert store:
                // http://blog.crazybob.org/2010/02/android-trusting-ssl-certificates.html
                InputStream in = context.getResources().openRawResource(certStoreResId);
                try {
                    trusted.load(in, "testingrocks".toCharArray());
                } finally {
                    in.close();
                }
            }
            return new SSLSocketFactory(trusted);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public static class Response {
        private int statusCode;
        private InputStream responseBody;

        public Response(HttpResponse httpResponse) throws IOException {
            statusCode = httpResponse.getStatusLine().getStatusCode();
            responseBody = httpResponse.getEntity().getContent();
        }

        public int getStatusCode() {
            return statusCode;
        }

        public InputStream getResponseBody() {
            return responseBody;
        }
    }
}
