package com.community.mail.client;

import com.community.mail.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@FeignClient(name = "email-service", path = "/v1", url = "http://localhost:8086")
public interface MailClient {

    @PostMapping(path = "/send")
    String send(@RequestBody @Valid EmailRequest request);
}
