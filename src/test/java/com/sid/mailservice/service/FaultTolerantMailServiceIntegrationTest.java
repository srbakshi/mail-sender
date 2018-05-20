package com.sid.mailservice.service;

import com.sid.mailservice.config.ApplicationConfiguration;
import com.sid.mailservice.model.SendMailResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * Integration Test (not to be run during build as it depends on external systems)
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class FaultTolerantMailServiceIntegrationTest extends BaseTest {

    @Autowired
    MailService mailService;

    @Test
    public void test() {
        SendMailResponse sendMailResponse = mailService.sendMail(getMockRequest());
        Assert.assertNotNull(sendMailResponse);
        Assert.assertNotNull(sendMailResponse.getId());
    }
}