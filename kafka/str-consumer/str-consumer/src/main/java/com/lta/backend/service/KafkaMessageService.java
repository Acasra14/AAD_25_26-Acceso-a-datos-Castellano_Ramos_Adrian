package com.lta.backend.service;

import com.lta.backend.model.KafkaMessage;
import com.lta.backend.repository.KafkaMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {

    private final KafkaMessageRepository kafkaMessageRepository;

    @Transactional
    public KafkaMessage saveMessage(String content) {
        KafkaMessage message = new KafkaMessage();
        message.setContent(content);
        message.setReceivedAt(LocalDateTime.now());
        return kafkaMessageRepository.save(message);
    }
}

