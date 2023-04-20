package com.example.main.service.impl;

import com.example.emailservice.dto.MailParam;
import com.example.main.common.Constant;
import com.example.main.common.CryptoTool;
import com.example.main.entity.AppUser;
import com.example.main.enums.AppUserState;
import com.example.main.service.AppUserService;
import com.example.main.service.EmailSender;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j
public class EmailSenderImpl implements EmailSender {
    @Value("${service.email.uri}")
    private String uri;
    private final AppUserService appUserService;
    private final CryptoTool cryptoTool;

    public EmailSenderImpl(AppUserService appUserService, CryptoTool cryptoTool) {
        this.appUserService = appUserService;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public String sendToEmail(AppUser appUser) {
        ResponseEntity<String> response = sendRequestToMailService(appUser);

        if (response.getStatusCode() != HttpStatus.OK)
            return "Sending the email failed";

        appUser.setEmail(appUser.getEmail());
        appUser.setUserState(AppUserState.WAIT_ACTIVATION_STATE);

        appUserService.save(appUser);

        return Constant.SENT_ACTIVATE_LINK.replace("{email}", appUser.getEmail());
    }

    public ResponseEntity<String> sendRequestToMailService(AppUser appUser) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MailParam mailParam = MailParam.builder()
                .userId(cryptoTool.getHashOf(appUser.getId()))
                .sendTo(appUser.getEmail())
                .userName(appUser.getFirstName())
                .build();

        HttpEntity<MailParam> request = new HttpEntity<>(mailParam, headers);

        return restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
    }
}