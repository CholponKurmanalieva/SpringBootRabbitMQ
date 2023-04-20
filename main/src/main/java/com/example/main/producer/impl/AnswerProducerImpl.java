package com.example.main.producer.impl;

import com.example.main.common.RabbitConstant;
import com.example.main.producer.AnswerProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class AnswerProducerImpl implements AnswerProducer {
    private final RabbitTemplate rabbitTemplate;

    public AnswerProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendAnswerToTelegramBot(SendMessage response) {
        rabbitTemplate.convertAndSend(RabbitConstant.ANSWER_EXCHANGE, null, response);
    }
}