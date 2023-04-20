package com.example.main.consumer;

import com.example.main.common.Constant;
import com.example.main.common.RabbitConstant;
import com.example.main.entity.AppUser;
import com.example.main.producer.AnswerProducer;
import com.example.main.service.AppUserService;
import com.example.main.service.CommonService;
import com.example.main.service.impl.EmailSenderImpl;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.core.ExchangeTypes;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

@Component
@Log4j
public class TextConsumer {
    private final EmailSenderImpl emailSender;
    private final AppUserService appUserService;
    private final AnswerProducer answerProducer;
    private final CommonService commonService;

    public TextConsumer(EmailSenderImpl emailSender, AppUserService appUserService, AnswerProducer answerProducer, CommonService commonService) {
        this.emailSender = emailSender;
        this.appUserService = appUserService;
        this.answerProducer = answerProducer;
        this.commonService = commonService;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitConstant.TEXT_QUEUE, durable = "true"),
            exchange = @Exchange(value = RabbitConstant.PROCESS_EXCHANGE, type = ExchangeTypes.FANOUT)))
    public void processTextMessage(Message message) {
        switch (message.getText()) {
            case "/activation":
                sendMessageAgain(message);
                break;
            case "/help":
                help(message);
                break;
            default:
                invalidCommand(message);
        }
    }

    private void sendMessageAgain(Message message) {
        commonService.saveBotRequest(message);

        AppUser appUser = appUserService.getUserByTelegramUserId(message.getFrom().getId());

        if (Objects.nonNull(appUser)) {
            ResponseEntity<String> response = emailSender.sendRequestToMailService(appUser);
            if (HttpStatus.OK == response.getStatusCode()) {
                answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.SENT_ACTIVATION_LINK_AGAIN));
            } else {
                answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.SENT_ACTIVATION_LINK_FAILED));
            }
        }
    }

    private void invalidCommand(Message message) {
        answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.INVALID_COMMAND));
    }

    private void help(Message message) {
        answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.HELP));
    }
}