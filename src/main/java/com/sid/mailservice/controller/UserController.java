package com.sid.mailservice.controller;

import com.sid.mailservice.model.ErrorResponse;
import com.sid.mailservice.model.SendMailRequest;
import com.sid.mailservice.model.SendMailResponse;
import com.sid.mailservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/mail")
public class UserController {

    @Autowired
    private MailService mailService;

    @PostMapping(path = "/send")
    public SendMailResponse sendMail(@Valid @RequestBody @NotNull SendMailRequest request) {
        return mailService.sendMail(request);
    }

    @ExceptionHandler({ Exception.class })
    public ErrorResponse handleException() {
        return new ErrorResponse("Request not valid");
    }

}
