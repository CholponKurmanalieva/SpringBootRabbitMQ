package com.example.main.entity;

import com.example.main.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "binary_content")
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContent extends BaseEntity {
    @Lob
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private byte[] fileAsArrayOfByte;
}