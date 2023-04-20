package com.example.main.service.impl;

import com.example.main.entity.AppDocument;
import com.example.main.repository.AppDocumentRepository;
import com.example.main.service.AppDocumentService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppDocumentServiceImpl implements AppDocumentService {
    private final AppDocumentRepository appDocumentRepository;

    public AppDocumentServiceImpl(AppDocumentRepository appDocumentRepository) {
        this.appDocumentRepository = appDocumentRepository;
    }

    @Override
    public AppDocument getDocumentById(Long id) {
        return appDocumentRepository.findById(id).orElse(null);
    }
}
