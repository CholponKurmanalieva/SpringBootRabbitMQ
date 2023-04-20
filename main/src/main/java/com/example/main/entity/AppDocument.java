package com.example.main.entity;

import com.example.main.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_document")
public class AppDocument extends BaseEntity {
    @Column(name = "telegram_file_id")
    private String telegramFileId;
    @Column(name = "doc_name")
    private String docName;
    @OneToOne
    private BinaryContent binaryContent;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "file_size")
    private Long fileSize;
}