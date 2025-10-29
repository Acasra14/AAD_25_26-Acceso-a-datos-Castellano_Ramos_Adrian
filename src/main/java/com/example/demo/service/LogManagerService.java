package com.example.demo.service;

import com.example.demo.model.LogEntry;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar operaciones de logging
 * Permite añadir eventos, filtrar por fecha y configurar codificación
 */
@Service
public class LogManagerService {

    /**
     * Codificación actual del archivo de logs
     */
    private String encoding = "UTF-8";

    /**
     * Archivo principal de logs (en raíz del proyecto)
     */
    private final String logFile = "app.log";

    /**
     * Formatter para timestamps
     */
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Añade un evento al fichero de log con timestamp actual
     * @param message Mensaje del evento a registrar
     * @throws Exception Si ocurre error de escritura en el archivo
     */
    public void addLogEvent(String message) throws Exception {
        // Obtener timestamp actual formateado
        String timestamp = LocalDateTime.now().format(timestampFormatter);

        // Escribe con la codificación actual en app.log
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(logFile, true),
                encoding.equals("UTF-8") ? StandardCharsets.UTF_8 : StandardCharsets.ISO_8859_1)) {

            writer.write("[" + timestamp + "] " + message + System.lineSeparator());
        }
    }

    /**
     * Filtra eventos por fecha (formato YYYY-MM-DD)
     * @param date Fecha para filtrar en formato YYYY-MM-DD
     * @return Lista de eventos que coinciden con la fecha
     * @throws Exception Si ocurre error de lectura del archivo
     */
    public List<LogEntry> filterEventsByDate(String date) throws Exception {
        List<LogEntry> events = new ArrayList<>();
        File file = new File(logFile);

        // Si no existe el archivo, devuelve la lista vacía
        if (!file.exists()) {
            return events;
        }

        // Lee el archivo con la codificación configurada
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file),
                encoding.equals("UTF-8") ? StandardCharsets.UTF_8 : StandardCharsets.ISO_8859_1))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Busca las líneas que contengan la fecha proporcionada
                if (line.contains("[" + date)) {
                    int openingBracket = line.indexOf("[");
                    int closingBracket = line.indexOf("]");
                    if (openingBracket != -1 && closingBracket != -1) {
                        // Extrae el timestamp y mensaje
                        String timestamp = line.substring(openingBracket + 1, closingBracket);
                        String message = line.substring(closingBracket + 2); // +2 para saltar "] "
                        events.add(new LogEntry(timestamp, message));
                    }
                }
            }
        }

        return events;
    }

    /**
     * Lee todos los eventos del archivo de logs
     * @return Lista con todos los eventos
     * @throws Exception Si ocurre error de lectura del archivo
     */
    public List<LogEntry> getAllEvents() throws Exception {
        List<LogEntry> events = new ArrayList<>();
        File file = new File(logFile);

        // Si no existe el archivo, devuelve la lista vacía
        if (!file.exists()) {
            return events;
        }

        // Lee el archivo con la codificación configurada
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file),
                encoding.equals("UTF-8") ? StandardCharsets.UTF_8 : StandardCharsets.ISO_8859_1))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Procesa todas las líneas que tengan formato de log
                int openingBracket = line.indexOf("[");
                int closingBracket = line.indexOf("]");
                if (openingBracket != -1 && closingBracket != -1) {
                    String timestamp = line.substring(openingBracket + 1, closingBracket);
                    String message = line.substring(closingBracket + 2);
                    events.add(new LogEntry(timestamp, message));
                }
            }
        }

        return events;
    }

    /**
     * Configura la codificación del fichero de logs
     * @param newEncoding Nueva codificación (UTF-8 o ISO-8859-1)
     */
    public void setEncoding(String newEncoding) {
        if ("UTF-8".equalsIgnoreCase(newEncoding) || "ISO-8859-1".equalsIgnoreCase(newEncoding)) {
            this.encoding = newEncoding.toUpperCase();
        }
    }
}