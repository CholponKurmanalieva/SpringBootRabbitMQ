package com.example.main.entity;

import com.example.main.entity.base.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONString;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bot_requests")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class BotRequest extends BaseEntity {
    @CreationTimestamp
    @Column(name = "requestDate")
    private LocalDateTime requestDate;

    @Type(type = "json")
    @Column(name = "request", columnDefinition = "json")
    private Message request;
}