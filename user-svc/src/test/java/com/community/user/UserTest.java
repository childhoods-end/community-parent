package com.community.user;


import com.community.mail.dto.EmailRequest;
import com.community.user.client.UserClient;
import com.community.user.controller.UserController;
import com.community.user.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApiApplication.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
@EnableFeignClients(basePackages = {"com.community"})
public class UserTest{



    //@Autowired
    //private MailClient mailClient;

    @Autowired
    private UserClient userClient;

    @Test
    public void test1(){
        /***
        Context context = new Context();
        context.setVariable("email", "s1299078636@sina.com");

        String url = "http://localhost:8083/v1/user/activation//101/code";
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("s1299078636@sina.com");
        emailRequest.setSubject("激活账号");
        emailRequest.setContext(content);
        mailClient.send(emailRequest);
         ***/

        Context context = new Context();
        context.setVariable("username", "sunday");
        TemplateEngine templateEngine = new TemplateEngine();
        // 通过添加模板路径主动调用
        //String content = templateEngine.process("/mail/demo", context);

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("1299078636@qq.com");
        emailRequest.setSubject("HTML");
        emailRequest.setContext("welcome");
        //mailClient.send(emailRequest);
    }

    @Test
    public void test2(){
        userClient.findConversationCount(111);
    }
}
