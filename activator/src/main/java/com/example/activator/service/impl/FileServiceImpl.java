package com.example.activator.service.impl;

import com.example.activator.service.FileService;
import com.example.main.common.CryptoTool;
import com.example.main.entity.AppDocument;
import com.example.main.entity.AppPhoto;
import com.example.main.entity.BinaryContent;
import com.example.main.service.AppDocumentService;
import com.example.main.service.AppPhotoService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
@Log4j
public class FileServiceImpl implements FileService {
    private final AppDocumentService appDocumentService;
    private final AppPhotoService appPhotoService;
    private final CryptoTool cryptoTool;

    public FileServiceImpl(AppDocumentService appDocumentService, AppPhotoService appPhotoService, CryptoTool cryptoTool) {
        this.appDocumentService = appDocumentService;
        this.appPhotoService = appPhotoService;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public AppDocument getDocumentById(String hash) {
        Long id = cryptoTool.getIdOf(hash);

        if (Objects.nonNull(id))
            return appDocumentService.getDocumentById(cryptoTool.getIdOf(hash));
        else
            return null;
    }

    @Override
    public AppPhoto getPhotoById(String hash) {
        return appPhotoService.getPhotoById(cryptoTool.getIdOf(hash));
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            File temp = File.createTempFile("tempFile", "bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfByte());

            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
