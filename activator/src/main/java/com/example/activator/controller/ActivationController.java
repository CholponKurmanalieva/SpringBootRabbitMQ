package com.example.activator.controller;

import com.example.activator.service.ActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activator")
public class ActivationController {
    private final ActivationService activationService;

    public ActivationController(ActivationService activationService) {
        this.activationService = activationService;
    }

    @GetMapping("/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String hashId) {
        boolean isActivate = activationService.activation(hashId);

        if (isActivate)
            return ResponseEntity.ok().body("Registration has been successfully completed!");
        else
            return ResponseEntity.internalServerError().body("An error has occurred please try again later");
    }
}