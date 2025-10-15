package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

@Slf4j
@Service
public class AñadirAlumno {

    // CONSTANTES: definen el tamaño fijo de los registros
    private static final int TAM_NOMBRE = 20; // 20 caracteres para el nombre
    // Tamaño total del registro: ID(4) + Nombre(20*2) + Nota(8) = 52 bytes
    private static final int TAM_REGISTRO = 4 + (2 * TAM_NOMBRE) + 8;
    private static final String FICHERO = "alumnos.dat"; // Nombre del archivo

    // Metodo principal para añadir un nuevo alumno al fichero
    public void añadir() {
        // Scanner para leer la entrada del usuario por teclado
        Scanner sc = new Scanner(System.in);
        try {
            // Mostrar cabecera y pedir datos al usuario
            log.info("--- AÑADIR NUEVO ALUMNO (Acceso secuencial) ---");
            log.info("Introduce el ID del alumno: ");
            int id = Integer.parseInt(sc.nextLine()); // Leer ID como número

            log.info("Introduce el nombre del alumno: ");
            String nombre = sc.nextLine(); // Leer nombre como texto

            log.info("Introduce la nota del alumno: ");
            double nota = Double.parseDouble(sc.nextLine()); // Leer nota como decimal

            // Abrir el fichero en modo lectura/escritura (rw)
            try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw")) {
                // Ir al FINAL del fichero (acceso secuencial)
                raf.seek(raf.length());

                // Escribir los datos en el fichero binario:
                raf.writeInt(id);                    // Escribir ID (4 bytes)
                escribirNombreFijo(raf, nombre);     // Escribir nombre (40 bytes - 20 chars)
                raf.writeDouble(nota);               // Escribir nota (8 bytes)

                // Confirmar que se guardó correctamente
                log.info("Alumno añadido correctamente: ID={}, Nombre={}, Nota={}", id, nombre, nota);
            }
        } catch (NumberFormatException e) {
            // Error si el usuario no introduce números válidos para ID o nota
            log.error("Error: Debes introducir un número válido para ID o nota.");
        } catch (Exception e) {
            // Cualquier otro error durante el proceso
            log.error("Error al añadir alumno: {}", e.getMessage());
        }
    }

    // Metodo auxiliar para escribir el nombre con TAMAÑO FIJO en el fichero
    private void escribirNombreFijo(RandomAccessFile raf, String nombre) throws IOException {
        // StringBuilder para manipular el nombre fácilmente
        StringBuilder sb = new StringBuilder(nombre);

        // Si el nombre es MÁS LARGO de 20 caracteres, lo cortamos
        if (sb.length() > TAM_NOMBRE) {
            sb.setLength(TAM_NOMBRE);
        } else {
            // Si el nombre es MÁS CORTO de 20 caracteres, lo rellenamos con espacios
            while (sb.length() < TAM_NOMBRE) {
                sb.append(' ');
            }
        }
        // Escribir el nombre como caracteres en el fichero (cada char son 2 bytes)
        raf.writeChars(sb.toString());
    }
}