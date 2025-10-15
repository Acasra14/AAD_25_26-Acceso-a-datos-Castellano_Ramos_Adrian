package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

@Slf4j
@Service
public class ModificarAlumno  {

    // CONSTANTES: mismas que las otras clases para consistencia
    private static final int TAM_NOMBRE = 20; // 20 caracteres para el nombre
    private static final int TAM_REGISTRO = 4 + (2 * TAM_NOMBRE) + 8; // 52 bytes por alumno
    private static final String FICHERO = "alumnos.dat"; // Mismo archivo

    // Metodo para modificar SOLO la nota de un alumno
    public void modificar() {
        Scanner sc = new Scanner(System.in);
        try {
            log.info("--- MODIFICAR NOTA DE ALUMNO ---");
            log.info("Introduce la posición del alumno a modificar: ");
            int posicion = Integer.parseInt(sc.nextLine()); // Leer posición

            log.info("Introduce la nueva nota: ");
            double nuevaNota = Double.parseDouble(sc.nextLine()); // Leer nueva nota

            // Abrir fichero para lectura y escritura ("rw")
            try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw")) {

                // Cálculo PRECISO de dónde está la NOTA dentro del registro:
                // posición * tamaño_registro + saltar_id(4) + saltar_nombre(40)
                long pos = (long) posicion * TAM_REGISTRO + 4 + (2 * TAM_NOMBRE);

                // VERIFICAR que el alumno existe en esa posición
                if (pos >= raf.length()) {
                    log.info("No existe un alumno en la posición {}", posicion);
                    return; // Salir si no existe
                }

                // SALTAR DIRECTAMENTE a la posición exacta de la NOTA
                raf.seek(pos);

                // SOBREESCRIBIR solo la nota (8 bytes)
                raf.writeDouble(nuevaNota);

                log.info("Nota modificada correctamente en la posición {}", posicion);
            }
        } catch (NumberFormatException e) {
            // Error si no introduce números válidos
            log.error("Error: Debes introducir números válidos para posición y nota.");
        } catch (Exception e) {
            // Cualquier otro error durante la modificación
            log.error("Error al modificar nota: {}", e.getMessage());
        }
    }
}