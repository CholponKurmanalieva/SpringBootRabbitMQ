package com.example.main.service.impl;

import com.example.main.entity.AppPhoto;
import com.example.main.repository.AppPhotoRepository;
import com.example.main.service.AppPhotoService;
import org.springframework.stereotype.Service;

@Service
public class AppPhotoServiceImpl implements AppPhotoService {
    private final AppPhotoRepository appPhotoRepository;

    public AppPhotoServiceImpl(AppPhotoRepository appPhotoRepository) {
        this.appPhotoRepository = appPhotoRepository;
    }

    @Override
    public AppPhoto getPhotoById(Long id) {
        return appPhotoRepository.findById(id).orElse(null);
    }
}
