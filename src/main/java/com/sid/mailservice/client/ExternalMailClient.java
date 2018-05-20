package com.sid.mailservice.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.mailservice.model.SendMailRequest;
import com.sid.mailservice.model.SendMailResponse;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class ExternalMailClient {
    private static final Logger logger = LoggerFactory.getLogger(ExternalMailClient.class);

    public SendMailResponse send(SendMailRequest request) {
        try {
            return sendInternal(request);
        } catch (Exception e) {
            SendMailResponse sendMailResponse = new SendMailResponse();
            sendMailResponse.setMessage(e.toString());
            sendMailResponse.setSuccess(false);
            return sendMailResponse;
        }
    }

    protected static <T> T processResponse(HttpResponse response, Class<T> valueType) throws IOException {
        String responseString = EntityUtils.toString(response.getEntity());
        logger.info("Response: " + responseString);
        EntityUtils.consume(response.getEntity());
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        T t;
        try {
            t = om.readValue(responseString, valueType);
        } catch (Exception e) {
          return null;
        }
        return t;
    }

    protected abstract SendMailResponse sendInternal(SendMailRequest request) throws IOException;

}
