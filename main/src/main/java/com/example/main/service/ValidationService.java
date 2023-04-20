package com.example.main.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface ValidationService {
    void validate(Message message);
}