package com.example.emailservice.service;

import com.example.emailservice.dto.MailParam;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailSender {
    void send(MailParam mailParam);
}