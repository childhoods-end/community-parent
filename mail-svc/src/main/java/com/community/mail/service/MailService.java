package com.community.mail.service;

import com.community.mail.dto.EmailRequest;
import com.community.mail.props.AppProps;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AppProps appProps;

    public void sendMail(EmailRequest request) {
        try {
            // 空的实例，需要填入内容
            MimeMessage message = mailSender.createMimeMessage();
            // 调用 MimeMessageHelper,传入 message 来完成邮件的生成
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(appProps.getFrom());   //发件人
            helper.setTo(request.getTo());       //收件人
            helper.setSubject(request.getSubject());             // 标题
            // 不加 true 则是普通文本，true 后可发送 html 文件
            helper.setText(request.getContext(), true);     // 内容
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            // e.getMessage() 异常信息
            System.out.println("发送邮件失败:" + e.getMessage());
        }
    }
}
