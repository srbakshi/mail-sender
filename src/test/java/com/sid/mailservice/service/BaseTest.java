package com.sid.mailservice.service;

import com.sid.mailservice.model.SendMailRequest;
import com.sid.mailservice.model.SendMailResponse;

public class BaseTest {

    protected final SendMailResponse SUCCESS = getSendMailResponse(true);
    protected final SendMailResponse FAIL = getSendMailResponse(false);

    protected SendMailRequest getMockRequest() {
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setTo("srbakshi@gmail.com");
        sendMailRequest.setFrom("srbakshi@gmail.com");
        sendMailRequest.setSubject("This is a subject");
        sendMailRequest.setText("Text message");
        return sendMailRequest;
    }

    protected SendMailResponse getSendMailResponse(boolean success) {
        SendMailResponse response = new SendMailResponse();
        response.setSuccess(success);
        response.setMessage("Sent mail");
        response.setId("1234");
        return response;
    }

}
