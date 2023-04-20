package com.example.activator.service;

import com.example.main.entity.AppDocument;
import com.example.main.entity.AppPhoto;
import com.example.main.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocumentById(String hash);
    AppPhoto getPhotoById(String hash);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}