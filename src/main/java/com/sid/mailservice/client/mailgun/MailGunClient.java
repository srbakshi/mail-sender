package com.sid.mailservice.client.mailgun;

import com.sid.mailservice.client.ExternalMailClient;
import com.sid.mailservice.client.mailgun.model.Response;
import com.sid.mailservice.model.SendMailRequest;
import com.sid.mailservice.model.SendMailResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MailGunClient extends ExternalMailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailGunClient.class);
    @Value("${mailgun.apikey}")
    private String apiKey;
    @Value("${mailgun.url}")
    private String url;

    @Autowired
    private HttpClient httpClient;

    @Override
    protected SendMailResponse sendInternal(SendMailRequest request) throws IOException {
        List<NameValuePair> form = getNameValuePairs(request);
        HttpPost post = getHttpPost(new UrlEncodedFormEntity(form, Consts.UTF_8));
        HttpResponse response = httpClient.execute(post);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            Response responseObject = processResponse(response, Response.class);
            if (responseObject != null) {
                return new SendMailResponse(responseObject.getId(), responseObject.getMessage(), !StringUtils.isEmpty(responseObject.getId()));
            } else {
                return new SendMailResponse(UUID.randomUUID().toString(), "", true);
            }
        }
        throw new RuntimeException("Request did not complete. Status code: " + statusLine.getStatusCode() + "-" + statusLine.getReasonPhrase());
    }

    private List<NameValuePair> getNameValuePairs(SendMailRequest request) {
        List<NameValuePair> form = new ArrayList<>();
        form.add(new BasicNameValuePair("from", request.getFrom()));
        form.add(new BasicNameValuePair("to", request.getTo()));
        form.add(new BasicNameValuePair("subject", (request.getSubject()) + System.currentTimeMillis()));
        form.add(new BasicNameValuePair("text", request.getText()));
        return form;
    }

    private HttpPost getHttpPost(UrlEncodedFormEntity entity) {
        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        post.setHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        post.setHeader(HttpHeaders.AUTHORIZATION, getAuthHeader());
        return post;
    }

    private String getAuthHeader() {
        String auth = "api:" + apiKey;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }

}
