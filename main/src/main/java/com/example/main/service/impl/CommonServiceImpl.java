package com.example.main.service.impl;

import com.example.main.common.CryptoTool;
import com.example.main.entity.BinaryContent;
import com.example.main.enums.LinkType;
import com.example.main.repository.BinaryContentRepository;
import com.example.main.service.BotRequestService;
import com.example.main.service.CommonService;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Service
@Log4j
public class CommonServiceImpl implements CommonService {
    @Value("${service.file_info.uri}")
    private String fileInfoUri;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${link.address.download}")
    private String addressForDownload;

    @Value("${service.file_storage.uri}")
    private String storageUri;

    private final BinaryContentRepository binaryContentRepository;
    private final BotRequestService botRequestService;
    private final CryptoTool cryptoTool;

    public CommonServiceImpl(BinaryContentRepository binaryContentRepository, BotRequestService botRequestService, CryptoTool cryptoTool) {
        this.binaryContentRepository = binaryContentRepository;
        this.botRequestService = botRequestService;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public SendMessage sendMessageGenerator(Message message, String text) {
        SendMessage response = new SendMessage();

        response.setChatId(message.getChatId());
        if (Objects.nonNull(text))
            response.setText(text);

        return response;
    }

    @Override
    public ResponseEntity<String> getFileInfo(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(fileInfoUri, HttpMethod.GET, request, String.class, token, fileId);
    }

    @Override
    public BinaryContent getPersistentBinaryContent(ResponseEntity<String> response) {
        String filePath = getFilePath(response);
        byte[] fileInByte = download(filePath);

        BinaryContent binaryContent = BinaryContent.builder()
                .fileAsArrayOfByte(fileInByte)
                .build();

        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public void saveBotRequest(Message message) {
        botRequestService.save(message);
    }

    @Override
    public String generateLinkForDownload(Long fileId, LinkType linkType) {
        return "http://" + addressForDownload  + linkType + "/" + cryptoTool.getHashOf(fileId);
    }

    private byte[] download(String filePath) {
        String uri = storageUri.replace("{token}", token).replace("{filePath}", filePath);
        URL url = null;

        try {
            url = new URL(uri);
        }catch (MalformedURLException e) {
            log.error(e);
        }

        try(InputStream inputStream = url.openStream()) {
            return inputStream.readAllBytes();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFilePath(ResponseEntity<String> response) {
        JSONObject json = new JSONObject(response.getBody());

        return json.getJSONObject("result").getString("file_path");
    }
}