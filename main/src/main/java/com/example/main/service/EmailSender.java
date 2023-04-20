package com.example.main.service;

import com.example.main.entity.AppUser;

public interface EmailSender {
    String sendToEmail(AppUser appUser);
}