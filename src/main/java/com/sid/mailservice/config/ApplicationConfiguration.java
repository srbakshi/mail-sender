package com.sid.mailservice.config;


import com.sid.mailservice.client.ExternalMailClient;
import com.sid.mailservice.client.HttpClientProvider;
import com.sid.mailservice.client.mailgun.MailGunClient;
import com.sid.mailservice.client.sendgrid.SendGridClient;
import com.sid.mailservice.service.FaultTolerantMailService;
import com.sid.mailservice.service.MailService;
import org.apache.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.sid.mailservice")
@PropertySource(value={"classpath:/app.properties"}, ignoreResourceNotFound = true)
public class ApplicationConfiguration {
    @Bean
    public MailService mailService() {
        return new FaultTolerantMailService();
    }

    @Bean
    public ExternalMailClient mailGunClient() {
        return new MailGunClient();
    }
    @Bean
    public ExternalMailClient sendGridClient() {
        return new SendGridClient();
    }

    @Bean
    public HttpClient httpClient() {
        return new HttpClientProvider().get();
    }
}
