package com.sid.mailservice.service;

import com.sid.mailservice.model.SendMailRequest;
import com.sid.mailservice.model.SendMailResponse;

public interface MailService {

    SendMailResponse sendMail(SendMailRequest request);

}
