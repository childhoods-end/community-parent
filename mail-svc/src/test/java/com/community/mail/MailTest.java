package com.community.mail;

import com.community.mail.client.MailClient;
import com.community.mail.dto.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommunityMailSvcApplication.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
@EnableFeignClients(basePackages = {"com.community.mail.client"})
public class MailTest {

    @Autowired
    private MailClient mailClient;


    @Test
    public void test1(){
        Context context = new Context();
        context.setVariable("email", "s1299078636@sina.com");
        TemplateEngine templateEngine = new TemplateEngine();
        String url = "http://localhost:8083/v1/user/activation//101/code";
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("s1299078636@sina.com");
        emailRequest.setSubject("激活账号");
        emailRequest.setContext(content);
        mailClient.send(emailRequest);
    }
}
