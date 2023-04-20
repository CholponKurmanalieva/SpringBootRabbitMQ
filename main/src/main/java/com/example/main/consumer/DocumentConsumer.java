package com.example.main.consumer;

import com.example.main.common.Constant;
import com.example.main.common.RabbitConstant;
import com.example.main.entity.AppDocument;
import com.example.main.entity.BinaryContent;
import com.example.main.enums.LinkType;
import com.example.main.producer.AnswerProducer;
import com.example.main.repository.AppDocumentRepository;
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
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Log4j
public class DocumentConsumer {
    private final CommonService commonService;
    private final AppDocumentRepository appDocumentRepository;
    private final AnswerProducer answerProducer;

    public DocumentConsumer(CommonService commonService, AppDocumentRepository appDocumentRepository, AnswerProducer answerProducer) {
        this.commonService = commonService;
        this.appDocumentRepository = appDocumentRepository;
        this.answerProducer = answerProducer;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitConstant.DOCUMENT_QUEUE, durable = "true"),
            exchange = @Exchange(value = RabbitConstant.PROCESS_EXCHANGE, type = ExchangeTypes.FANOUT)))
    public void processDocumentMessage(Message message) {
        commonService.saveBotRequest(message);

       try {
           AppDocument appDocument = processDocument(message);

           String linkForDownload = commonService.generateLinkForDownload(appDocument.getId(), LinkType.DOC_DOWNLOAD);

           answerProducer.sendAnswerToTelegramBot(
                   commonService.sendMessageGenerator(
                           message, Constant.DOWNLOAD_LINK.replace("{link}", linkForDownload)));
       }catch (Exception e) {
           log.error(e);
           answerProducer.sendAnswerToTelegramBot(commonService.sendMessageGenerator(message, Constant.DOWNLOAD_LINK_ERROR));
       }
    }

    private AppDocument processDocument(Message message) {
        try {
            ResponseEntity<String> response = commonService.getFileInfo(message.getDocument().getFileId());

            if (HttpStatus.OK == response.getStatusCode()) {
                BinaryContent binaryContent = commonService.getPersistentBinaryContent(response);

                return buildAndSaveTransientDocument(message.getDocument(), binaryContent);
            }
            return null;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AppDocument buildAndSaveTransientDocument(Document document, BinaryContent binaryContent) {
        AppDocument appDocument = AppDocument.builder()
                .telegramFileId(document.getFileId())
                .docName(document.getFileName())
                .binaryContent(binaryContent)
                .mimeType(document.getMimeType())
                .fileSize(document.getFileSize())
                .build();

        return appDocumentRepository.save(appDocument);
    }
}
