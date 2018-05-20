package com.sid.mailservice.service;

import com.sid.mailservice.client.ExternalMailClient;
import com.sid.mailservice.model.SendMailRequest;
import com.sid.mailservice.model.SendMailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FaultTolerantMailService implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(FaultTolerantMailService.class);

    @Autowired
    private ExternalMailClient mailGunClient;
    @Autowired
    private ExternalMailClient sendGridClient;

    @Override
    public SendMailResponse sendMail(SendMailRequest request) {
        SendMailResponse response = send(mailGunClient, request);
        if (!response.isSuccess()) {
            response = send(sendGridClient, request);
        }
        return response;
    }

    private SendMailResponse send(ExternalMailClient client, SendMailRequest request) {
        logger.info("#########Sending mail through " + client.getClass() + ".#########");
        SendMailResponse sendMailResponse = client.send(request);
        logStatus(client, sendMailResponse);
        return sendMailResponse;
    }

    private void logStatus(ExternalMailClient client, SendMailResponse sendMailResponse) {
        if (sendMailResponse.isSuccess()) {
            logger.info("Mail sent successfully. Reference Id: " + sendMailResponse.getId());
        } else {
            logger.info("!!!!Could not send mail through " + client.getClass() + ". " + sendMailResponse.getMessage());
        }
    }
}
