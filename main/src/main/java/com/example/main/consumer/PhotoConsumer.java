package com.example.main.consumer;

import com.example.main.common.Constant;
import com.example.main.common.RabbitConstant;
import com.example.main.entity.AppPhoto;
import com.example.main.entity.BinaryContent;
import com.example.main.enums.LinkType;
import com.example.main.producer.AnswerProducer;
import com.example.main.repository.AppPhotoRepository;
import com.example.main.service.CommonService;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

@Log4j
@Component
public class PhotoConsumer {
    private final CommonService commonService;
    private final AppPhotoRepository appPhotoRepository;
    private final AnswerProducer answerProducer;

    public PhotoConsumer(CommonService commonService, AppPhotoRepository appPhotoRepository, AnswerProducer answerProducer) {
        this.commonService = commonService;
        this.appPhotoRepository = appPhotoRepository;
        this.answerProducer = answerProducer;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitConstant.PHOTO_QUEUE, durable = "true"),
    exchange = @Exchange(value = RabbitConstant.PROCESS_EXCHANGE, type = ExchangeTypes.FANOUT)))
    public void processPhotoMessage(Message message) {
        commonService.saveBotRequest(message);

        try {
            AppPhoto appPhoto = processPhoto(message);

            String linkForDownload = commonService.generateLinkForDownload(appPhoto.getId(), LinkType.PHOTO_DOWNLOAD);

            answerProducer.sendAnswerToTelegramBot(
                    commonService.sendMessageGenerator(message, Constant.DOWNLOAD_LINK.replace("{link}", linkForDownload)));
        }catch (Exception e) {
            log.error(e);
            answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.DOWNLOAD_LINK_ERROR));
        }
    }

    private AppPhoto processPhoto(Message message) {
        int photoSizeCount = message.getPhoto().size();
        int photoIndex = photoSizeCount > 1 ? photoSizeCount - 1 : 0;
        PhotoSize photo = message.getPhoto().get(photoIndex);

        ResponseEntity<String> response = commonService.getFileInfo(photo.getFileId());

        if (HttpStatus.OK == response.getStatusCode()) {
            BinaryContent binaryContent = commonService.getPersistentBinaryContent(response);
            return buildAndSaveTransientPhoto(binaryContent, photo);
        }

        return null;
    }
    private AppPhoto buildAndSaveTransientPhoto (BinaryContent binaryContent, PhotoSize photoSize) {
        AppPhoto appPhoto = AppPhoto.builder()
                .telegramFileId(photoSize.getFileId())
                .binaryContent(binaryContent)
                .fileSize(photoSize.getFileSize())
                .build();

        return appPhotoRepository.save(appPhoto);
    }
}