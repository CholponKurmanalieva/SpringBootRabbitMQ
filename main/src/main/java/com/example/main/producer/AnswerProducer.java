package com.example.main.producer;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AnswerProducer {
    void sendAnswerToTelegramBot(SendMessage response);
}