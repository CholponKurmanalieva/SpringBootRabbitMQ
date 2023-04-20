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

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_photo")
public class AppPhoto extends BaseEntity {
    @Column(name = "telegram_file_id")
    private String telegramFileId;
    @OneToOne
    private BinaryContent binaryContent;
    @Column(name = "file_size")
    private Integer fileSize;
}