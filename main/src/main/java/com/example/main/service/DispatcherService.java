package com.example.main.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface DispatcherService {
    void sendMessage(SendMessage response);
    void dispatcherMessages(Message message);
}