package com.sid.mailservice.service;

import com.sid.mailservice.client.ExternalMailClient;
import com.sid.mailservice.model.SendMailRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

public class FaultTolerantMailServiceTest extends BaseTest {

    @Mock
    private ExternalMailClient mailGunClient;
    @Mock
    private ExternalMailClient sendGridClient;

    private FaultTolerantMailService faultTolerantMailService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        faultTolerantMailService = new FaultTolerantMailService();
        ReflectionTestUtils.setField(faultTolerantMailService, "mailGunClient", mailGunClient);
        ReflectionTestUtils.setField(faultTolerantMailService, "sendGridClient", sendGridClient);
    }

    @Test
    public void mailGunSuccess() {

        when(mailGunClient.send(any())).thenReturn(SUCCESS);

        SendMailRequest mockRequest = getMockRequest();
        faultTolerantMailService.sendMail(mockRequest);

        verify(mailGunClient, times(1)).send(any());
        verifyZeroInteractions(sendGridClient);
    }

    @Test
    public void mailGunFail_shouldFallback() {
        when(mailGunClient.send(any())).thenReturn(FAIL);
        when(sendGridClient.send(any())).thenReturn(SUCCESS);

        SendMailRequest mockRequest = getMockRequest();
        faultTolerantMailService.sendMail(mockRequest);

        verify(mailGunClient, times(1)).send(any());
        verify(sendGridClient, times(1)).send(any());
    }

}