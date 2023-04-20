package com.example.activator.controller;

import com.example.activator.exception.FileNotFoundException;
import com.example.activator.exception.FileSystemResourceException;
import com.example.activator.service.FileService;
import com.example.main.entity.AppDocument;
import com.example.main.entity.AppPhoto;
import com.example.main.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/doc/{id}")
    public ResponseEntity<?> getDocument(@PathVariable("id") String hash) throws Exception {
        AppDocument appDocument = fileService.getDocumentById(hash);

        if (Objects.nonNull(appDocument)) {
            BinaryContent binaryContent = appDocument.getBinaryContent();
            FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);

            if (Objects.nonNull(fileSystemResource))
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(appDocument.getMimeType()))
                        .header("Content-disposition", "attachment; filename=" + appDocument.getDocName())
                        .body(fileSystemResource);
            else
                throw new FileSystemResourceException("File System Resource Exception");
        } else
            throw new FileNotFoundException("File not found!");
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable("id") String hash) throws Exception {
        AppPhoto appPhoto = fileService.getPhotoById(hash);

        if (Objects.nonNull(appPhoto)) {
            BinaryContent binaryContent = appPhoto.getBinaryContent();
            FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);

            if (Objects.nonNull(fileSystemResource))
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header("Content-disposition", "attachment")
                        .body(fileSystemResource);
            else
                throw new FileSystemResourceException("ile System Resource Exception");
        } else
            throw new FileNotFoundException("File not found!");
    }
}