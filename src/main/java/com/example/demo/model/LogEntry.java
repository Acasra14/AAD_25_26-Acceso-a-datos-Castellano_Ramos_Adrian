package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una entrada de log con timestamp y mensaje
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {

    /**
     * Timestamp del evento en formato YYYY-MM-DD HH:mm:ss
     */
    private String timestamp;

    /**
     * Mensaje descriptivo del evento
     */
    private String message;
}