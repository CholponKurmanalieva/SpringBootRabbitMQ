package com.example.main.producer.impl;

import com.example.main.producer.DispatcherProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class DispatcherProducerImpl implements DispatcherProducer {
    private final RabbitTemplate rabbitTemplate;

    public DispatcherProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String queue, Message message) {
        rabbitTemplate.convertAndSend(queue, message);
    }
}
