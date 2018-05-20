package com.sid.mailservice.client;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientProvider {

    private HttpClient httpClient;

    public HttpClientProvider() {
        PoolingHttpClientConnectionManager pooledConnectionManager = new PoolingHttpClientConnectionManager();
        pooledConnectionManager.setMaxTotal(10);
        pooledConnectionManager.setDefaultMaxPerRoute(10);
        httpClient = HttpClients.custom().setConnectionManager(pooledConnectionManager).build();
    }

    public HttpClient get() {
        return httpClient;
    }
}
