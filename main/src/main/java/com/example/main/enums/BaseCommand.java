package com.example.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.*;

@AllArgsConstructor
public enum BaseCommand {
    HELP("/help", "Get help about base command"),
    ACTIVATION("/activation", "Send the activation link again");

    @Getter
    private final String command;
    @Getter
    private final String description;
    public static final List<BotCommand> storage = new ArrayList<>();

    static {
        Arrays.asList(values()).forEach(value -> storage.add(new BotCommand(value.getCommand(), value.getDescription())));
    }
}