package com.example.activator.service.impl;

import com.example.activator.service.ActivationService;
import com.example.main.common.CryptoTool;
import com.example.main.entity.AppUser;
import com.example.main.enums.AppUserState;
import com.example.main.service.AppUserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ActivationServiceImpl implements ActivationService {
    private final CryptoTool cryptoTool;
    private final AppUserService appUserService;

    public ActivationServiceImpl(CryptoTool cryptoTool, AppUserService appUserService) {
        this.cryptoTool = cryptoTool;
        this.appUserService = appUserService;
    }


    @Override
    public boolean activation(String hashId) {
        Long id = cryptoTool.getIdOf(hashId);

        AppUser appUser = appUserService.getById(id);

        if (Objects.nonNull(appUser)) {
            appUser.setActive(true);
            appUser.setUserState(AppUserState.BASIC_STATE);

            appUserService.save(appUser);

            return true;
        }
        return false;
    }
}