package com.example.main.service;

import com.example.main.entity.BinaryContent;
import com.example.main.enums.LinkType;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommonService {
    SendMessage sendMessageGenerator(Message message, String text);
    ResponseEntity<String> getFileInfo(String fileId);
    BinaryContent getPersistentBinaryContent(ResponseEntity<String> response);
    void saveBotRequest(Message message);
    String generateLinkForDownload(Long fileId, LinkType linkType);
}