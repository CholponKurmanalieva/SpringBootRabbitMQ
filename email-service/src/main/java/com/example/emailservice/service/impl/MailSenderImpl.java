package com.example.emailservice.service.impl;

import com.example.emailservice.dto.MailParam;
import com.example.emailservice.service.MailSender;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailSenderImpl implements MailSender {
    @Value("${spring.mail.username}")
    private String sendFrom;

    @Value("${activator.uri}")
    String activatorUri;

    private final JavaMailSender javaMailSender;

    public MailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(MailParam mailParam) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage);

        try {
            helper.setFrom(sendFrom, "Telegram Bot");
            helper.setTo(mailParam.getSendTo());
            helper.setSubject("Account activation");
            helper.setText(generateBody(mailParam), true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMailMessage);
    }

    private String generateLink(String userId) {
        return activatorUri.concat("?id=").concat(userId);
    }

    private String generateBody(MailParam mailParam) {
        StringBuilder body = new StringBuilder();
        body.append("<h2> Hello, ")
                .append(mailParam.getUserName())
                .append("</h2>")
                .append("<p>To complete the registration, follow the link</p>")
                .append(generateLink(mailParam.getUserId()));

        return body.toString();
    }
}