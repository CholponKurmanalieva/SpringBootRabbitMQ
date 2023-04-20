package com.example.main.service.impl;

import com.example.main.entity.BotRequest;
import com.example.main.repository.BotRequestRepository;
import com.example.main.service.BotRequestService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Log4j
public class BotRequestServiceImpl implements BotRequestService {
    private final BotRequestRepository botRequestRepository;

    public BotRequestServiceImpl(BotRequestRepository botRequestRepository) {
        this.botRequestRepository = botRequestRepository;
    }

    @Override
    public void save(Message message) {
        BotRequest request = BotRequest.builder()
                .request(message)
                .build();

        try {
            botRequestRepository.save(request);
        } catch (Exception e) {
            log.error("BotRequest does not save " + e);
        }
    }
}