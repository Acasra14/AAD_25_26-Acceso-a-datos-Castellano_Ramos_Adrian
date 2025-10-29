package com.example.demo;

import com.example.demo.model.LogEntry;
import com.example.demo.service.LogManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

/**
 * Aplicación principal para gestión de logs
 */
@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class LogManagerApplication implements CommandLineRunner {

    private final LogManagerService logManagerService;

    public static void main(String[] args) {
        SpringApplication.run(LogManagerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Iniciando Gestor de Logs...");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Menú principal
            log.info("");
            log.info("\n=== GESTOR DE LOGS ===");
            log.info("1. Añadir evento al log");
            log.info("2. Filtrar eventos por fecha");
            log.info("3. Cambiar codificación");
            log.info("4. Mostrar todos los logs");
            log.info("5. Salir");
            log.info("Selecciona una opción: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1" -> {
                    log.info("Introduce el mensaje del evento: ");
                    String message = scanner.nextLine();
                    try {
                        logManagerService.addLogEvent(message);
                        log.info("Evento añadido correctamente: {}", message);
                    } catch (Exception e) {
                        log.error("Error al añadir evento: {}", e.getMessage());
                    }
                }
                case "2" -> {
                    log.info("Introduce la fecha (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    try {
                        List<LogEntry> events = logManagerService.filterEventsByDate(date);
                        if (events.isEmpty()) {
                            log.info("No hay eventos para la fecha: {}", date);
                        } else {
                            log.info("Eventos encontrados para {}:", date);
                            for (LogEntry event : events) {
                                log.info("[{}] {}", event.getTimestamp(), event.getMessage());
                            }
                            log.info("Total de eventos: {}", events.size());
                        }
                    } catch (Exception e) {
                        log.error("Error al filtrar eventos: {}", e.getMessage());
                    }
                }
                case "3" -> {
                    log.info("=== CAMBIAR CODIFICACIÓN ===");
                    log.info("1. UTF-8");
                    log.info("2. ISO-8859-1");
                    log.info("Selecciona la codificación: ");

                    String encodingOption = scanner.nextLine();
                    switch (encodingOption) {
                        case "1" -> {
                            logManagerService.setEncoding("UTF-8");
                            log.info("Codificación cambiada a UTF-8");
                        }
                        case "2" -> {
                            logManagerService.setEncoding("ISO-8859-1");
                            log.info("Codificación cambiada a ISO-8859-1");
                        }
                        default -> log.error("Opción no válida: {}", encodingOption);
                    }
                }
                case "4" -> {
                    try {
                        List<LogEntry> allEvents = logManagerService.getAllEvents();
                        if (allEvents.isEmpty()) {
                            log.info("No hay eventos en el log");
                        } else {
                            log.info("=== TODOS LOS EVENTOS DEL LOG ===");
                            for (LogEntry event : allEvents) {
                                log.info("[{}] {}", event.getTimestamp(), event.getMessage());
                            }
                            log.info("Total de eventos: {}", allEvents.size());
                        }
                    } catch (Exception e) {
                        log.error("Error al leer todos los logs: {}", e.getMessage());
                    }
                }
                case "5" -> {
                    running = false;
                    log.info("Saliendo del Gestor de Logs...");
                }
                default -> log.error("Opción no válida: {}", option);
            }
        }
        scanner.close();
    }
}