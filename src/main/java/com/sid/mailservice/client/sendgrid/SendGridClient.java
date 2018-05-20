package com.sid.mailservice.client.sendgrid;

import com.sid.mailservice.client.ExternalMailClient;
import com.sid.mailservice.client.sendgrid.model.Error;
import com.sid.mailservice.client.sendgrid.model.Response;
import com.sid.mailservice.model.SendMailRequest;
import com.sid.mailservice.model.SendMailResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SendGridClient extends ExternalMailClient {

    @Value("${sendgrid.apikey}")
    private String apiKey;
    @Value("${sendgrid.url}")
    private String url;

    @Autowired
    private HttpClient httpClient;

    @Override
    protected SendMailResponse sendInternal(SendMailRequest request) throws IOException {
        String data = getData(request);
        HttpPost post = getHttpPost(data);
        HttpResponse response = httpClient.execute(post);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == 200 || statusLine.getStatusCode() == 202) {
            Response responseObject = processResponse(response, Response.class);
            if (responseObject != null) {
                List<Error> errors = responseObject.getErrors();
                String errorMessages = errors.stream().map(Error::getMessage).collect(Collectors.joining(","));
                return new SendMailResponse(UUID.randomUUID().toString(), errorMessages, errors.isEmpty());
            } else {
                return new SendMailResponse(UUID.randomUUID().toString(), "", true);
            }
        }
        throw new RuntimeException("Request did not complete. Status code: " + statusLine.getStatusCode() + "-" + statusLine.getReasonPhrase());
    }

    private String getData(SendMailRequest request) {

        return "{\"personalizations\": [{\"to\": [{\"email\": \"" + request.getTo() + "\"}]}],\"from\": {\"email\": \""
                + request.getFrom() + "\"},\"subject\": \"" + request.getSubject() + "\"" +
                ",\"content\": [{\"type\": \"text/plain\", \"value\": \"" + request.getText() + "\"}]}";

    }

    private HttpPost getHttpPost(String data) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(data));
        post.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        post.setHeader(HttpHeaders.AUTHORIZATION, getAuthHeader());
        return post;
    }

    private String getAuthHeader() {
        return "Bearer " + apiKey;
    }


}
