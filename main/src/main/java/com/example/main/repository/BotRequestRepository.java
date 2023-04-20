package com.example.main.repository;

import com.example.main.entity.BotRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRequestRepository extends JpaRepository<BotRequest, Long> {
}