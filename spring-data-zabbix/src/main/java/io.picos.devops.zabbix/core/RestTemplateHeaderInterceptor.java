package io.picos.devops.zabbix.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author dz
 */
public class RestTemplateHeaderInterceptor implements ClientHttpRequestInterceptor {

    private static final String APPLICATION_JSON = "application/json";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws
                                                                                                                IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
        return execution.execute(request, body);
    }

}