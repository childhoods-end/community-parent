package com.community.mail.controller;

import com.community.mail.dto.EmailRequest;
import com.community.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@Validated
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping(path = "/send")
    public String send(@RequestBody @Valid EmailRequest request){
        mailService.sendMail(request);
        return "email has been sent.";
    }
}
