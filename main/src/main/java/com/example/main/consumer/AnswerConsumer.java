package com.example.main.consumer;

import com.example.main.common.RabbitConstant;
import com.example.main.service.DispatcherService;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Log4j
public class AnswerConsumer {
    private final DispatcherService dispatcherService;

    public AnswerConsumer(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitConstant.ANSWER_QUEUE, durable = "true"),
    exchange = @Exchange(value = RabbitConstant.ANSWER_EXCHANGE, type = ExchangeTypes.FANOUT)))
    public void sendResponseToTelegramBot(SendMessage response) {
        dispatcherService.sendMessage(response);
    }
}