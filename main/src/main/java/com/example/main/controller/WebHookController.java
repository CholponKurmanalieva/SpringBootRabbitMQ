package com.example.main.controller;

import com.example.main.service.ValidationService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RestController
public class WebHookController {
    private final ValidationService validationService;

    public WebHookController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/callback/update")
    public ResponseEntity<?> onUpdateReceiver(@RequestBody Update update) {
        validationService.validate(update.getMessage());

        return ResponseEntity.ok().build();
    }
}