package com.lta.backend.repository;

import com.lta.backend.model.KafkaMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaMessageRepository extends JpaRepository<KafkaMessage, Long> {
}
