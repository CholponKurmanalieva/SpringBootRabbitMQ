package com.example.main.service.impl;

import com.example.main.common.Constant;
import com.example.main.entity.AppUser;
import com.example.main.enums.AppUserState;
import com.example.main.producer.AnswerProducer;
import com.example.main.service.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.main.enums.AppUserState.BASIC_STATE;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final AppUserService appUserService;
    private final AnswerProducer answerProducer;
    private final CommonService commonService;
    private final EmailSender emailSender;
    private final DispatcherService dispatcherService;

    public ValidationServiceImpl(AppUserService appUserService, AnswerProducer answerProducer, CommonService commonService, EmailSender emailSender, DispatcherService dispatcherService) {
        this.appUserService = appUserService;
        this.answerProducer = answerProducer;
        this.commonService = commonService;
        this.emailSender = emailSender;
        this.dispatcherService = dispatcherService;
    }

    @Override
    public void validate(Message message) {
        User telegramUser = message.getFrom();
        AppUser appUser = appUserService.getUserByTelegramUserId(telegramUser.getId());

        if (Objects.nonNull(appUser)) {
            if (Objects.nonNull(appUser.getEmail()) && appUser.getUserState().equals(AppUserState.WAIT_ACTIVATION_STATE))
                answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.ANSWER_IN_WAIT_ACTIVATION_STATE));
            else if (Objects.nonNull(message.getText()) && Objects.isNull(appUser.getEmail()))
                answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, checkAndSendMessageToEmail(message.getText(), appUser)));
            else if (Objects.isNull(appUser.getEmail()))
                answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.ANSWER_EMAIL_NULL));
            else if (BASIC_STATE.equals(appUser.getUserState()) && appUser.isActive())
                dispatcherService.dispatcherMessages(message);
            else if (Objects.nonNull(message.getText()))
                dispatcherService.dispatcherMessages(message);

        } else {
            AppUser transientUser = AppUser.builder()
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .telegramUserId(telegramUser.getId())
                    .isActive(false)
                    .userState(BASIC_STATE)
                    .build();

            appUserService.save(transientUser);
            answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.REGISTER));
        }
    }

    private String checkAndSendMessageToEmail(String email, AppUser appUser) {
        Pattern pattern = Pattern.compile("\\w+@(gmail.com)");
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            appUser.setEmail(email);

            return emailSender.sendToEmail(appUser);
        }

        return Constant.INCORRECT_EMAIL;
    }
}