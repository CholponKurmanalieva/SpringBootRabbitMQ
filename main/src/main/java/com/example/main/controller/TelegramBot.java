package com.example.main.controller;

import com.example.main.enums.BaseCommand;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Log4j
@Component
public class TelegramBot extends TelegramWebhookBot {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.name}")
    private String botName;
    @Value("${telegram.bot.uri}")
    private String botUri;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return "/update";
    }

    @PostConstruct
    public void init() {
        try {
            SetWebhook setWebhook = SetWebhook.builder()
                    .url(botUri)
                    .build();

            this.setWebhook(setWebhook);

            execute(new SetMyCommands(BaseCommand.storage, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }

    public void sendMessageToTelegram(SendMessage response) {
        if (Objects.nonNull(response)) {
            try {
                execute(response);
            } catch (TelegramApiException e) {
                log.error("Error occurred when sending a response to the telegram bot", e);
            }
        }
    }
}