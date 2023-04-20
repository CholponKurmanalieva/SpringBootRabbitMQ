package com.example.main.service;

import com.example.main.entity.AppUser;

public interface AppUserService {
    AppUser getUserByTelegramUserId(Long id);
    AppUser save(AppUser appUser);
    AppUser getById(Long id);
}