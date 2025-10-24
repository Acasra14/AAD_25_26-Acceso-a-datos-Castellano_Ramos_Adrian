package com.example.demo;

import com.example.demo.service.CsvConverterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class CsvConverterApplication implements CommandLineRunner {

    private final CsvConverterService csvConverterService;

    public static void main(String[] args) {
        SpringApplication.run(CsvConverterApplication.class, args);
    }

    /**
     * Metodo que se ejecuta al iniciar la aplicación
     * @param args Argumentos de línea de comandos
     */
    @Override
    public void run(String... args) {
        try {
            log.info("Iniciando conversión de CSV a JSON y XML...");
            String rutaCompleta = "src/main/resources/alumns.csv";
            csvConverterService.convertirCsv(rutaCompleta, "alumns.json", "alumns.xml");
            log.info("Conversión completada exitosamente");
        } catch (Exception e) {
            log.error("Error durante la conversión: {}", e.getMessage());
        }
    }
}