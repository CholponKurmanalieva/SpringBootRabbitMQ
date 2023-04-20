package com.example.main.repository;

import com.example.main.entity.AppDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
}