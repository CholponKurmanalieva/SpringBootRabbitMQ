package com.example.main.service.impl;

import com.example.main.entity.AppUser;
import com.example.main.repository.AppUserRepository;
import com.example.main.service.AppUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser getUserByTelegramUserId(Long id) {
        Optional<AppUser> appUser = appUserRepository.findAppUserByTelegramUserId(id);

        return appUser.orElse(null);
    }

    @Override
    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser getById(Long id) {
        Optional<AppUser> optional = appUserRepository.findById(id);

        return optional.orElse(null);
    }
}