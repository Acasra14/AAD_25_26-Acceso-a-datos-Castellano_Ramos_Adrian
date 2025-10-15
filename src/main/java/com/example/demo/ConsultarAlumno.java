package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

@Slf4j
@Service
public class ConsultarAlumno {

    // CONSTANTES: mismas que en AñadirAlumno para calcular posiciones correctamente
    private static final int TAM_NOMBRE = 20; // 20 caracteres para el nombre
    private static final int TAM_REGISTRO = 4 + (2 * TAM_NOMBRE) + 8; // 52 bytes por alumno
    private static final String FICHERO = "alumnos.dat"; // Mismo archivo

    // Metodo para consultar un alumno por su posición en el fichero
    public void consultar() {
        Scanner sc = new Scanner(System.in);
        try {
            log.info("--- CONSULTAR ALUMNO POR POSICIÓN ---");
            log.info("Introduce la posición del alumno a consultar: ");
            int posicion = Integer.parseInt(sc.nextLine()); // Leer posición como número

            // Abrir fichero solo para lectura ("r")
            try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "r")) {

                // Calcular la POSICIÓN EXACTA en bytes: posición * tamaño_registro
                long pos = (long) posicion * TAM_REGISTRO;

                // VERIFICAR que la posición existe en el fichero
                if (pos >= raf.length()) {
                    log.info("No existe un alumno en la posición {}", posicion);
                    return; // Salir si no existe
                }

                // SALTAR DIRECTAMENTE a la posición calculada
                raf.seek(pos);

                // LEER los datos del alumno desde esa posición:

                // 1. Leer ID (primeros 4 bytes)
                int id = raf.readInt();

                // 2. Leer nombre (siguientes 40 bytes - 20 caracteres)
                char[] nombreChars = new char[TAM_NOMBRE];
                for (int i = 0; i < TAM_NOMBRE; i++) {
                    nombreChars[i] = raf.readChar(); // Leer cada carácter
                }
                String nombre = new String(nombreChars).trim(); // Convertir a String y quitar espacios

                // 3. Leer nota (últimos 8 bytes)
                double nota = raf.readDouble();

                // Mostrar los datos del alumno encontrado
                log.info("Alumno en posición {}: ID={}, Nombre={}, Nota={}",
                        posicion, id, nombre, nota);
            }
        } catch (NumberFormatException e) {
            // Error si no introduce un número válido para la posición
            log.error("Error: Debes introducir un número válido para la posición.");
        } catch (Exception e) {
            // Cualquier otro error durante la consulta
            log.error("Error al consultar alumno: {}", e.getMessage());
        }
    }
}