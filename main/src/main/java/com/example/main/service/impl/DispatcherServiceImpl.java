package com.example.main.service.impl;

import com.example.main.common.RabbitConstant;
import com.example.main.controller.TelegramBot;
import com.example.main.producer.DispatcherProducer;
import com.example.main.service.DispatcherService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

@Service
public class DispatcherServiceImpl implements DispatcherService {
    private final TelegramBot telegramBot;
    private final DispatcherProducer dispatcherProducer;

    public DispatcherServiceImpl(TelegramBot telegramBot, DispatcherProducer dispatcherProducer) {
        this.telegramBot = telegramBot;
        this.dispatcherProducer = dispatcherProducer;
    }

    @Override
    public void sendMessage(SendMessage response) {
        telegramBot.sendMessageToTelegram(response);
    }

    @Override
    public void dispatcherMessages(Message message) {
        if (Objects.nonNull(message.getDocument()))
            dispatcherProducer.produce(RabbitConstant.DOCUMENT_QUEUE, message);
        if (Objects.nonNull(message.getPhoto()))
            dispatcherProducer.produce(RabbitConstant.PHOTO_QUEUE, message);
        if (Objects.nonNull(message.getText()))
            dispatcherProducer.produce(RabbitConstant.TEXT_QUEUE, message);
    }
}