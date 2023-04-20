package com.example.main.service;

import com.example.main.entity.BotRequest;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotRequestService {
    void save(Message message);
}