package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ExploradorFicheros {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduce la ruta de un directorio:");
        String rutaStr = sc.nextLine();
        File dir = new File(rutaStr);

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("El directorio no existe o no es válido.");
            return;
        }

        // Mostrar el contenido inicial del directorio
        mostrarContenido(dir);

        int opcion;
        do {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Crear un nuevo fichero vacío");
            System.out.println("2. Mover un fichero");
            System.out.println("3. Copiar un fichero");
            System.out.println("4. Borrar un fichero");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            try {
                switch (opcion) {
                    case 1: // Crear fichero
                        System.out.println("Introduce el nombre del nuevo fichero:");
                        String nuevo = sc.nextLine();
                        File nuevoFich = new File(dir, nuevo);
                        if (nuevoFich.createNewFile()) {
                            System.out.println("Fichero creado correctamente.");
                        } else {
                            System.out.println("No se pudo crear (quizás ya existe).");
                        }
                        break;

                    case 2: // Mover fichero
                        System.out.println("Introduce el nombre del fichero a mover:");
                        String origenStr = sc.nextLine();
                        File origen = new File(dir, origenStr);
                        if (!origen.exists()) {
                            System.out.println("El fichero no existe.");
                            break;
                        }
                        System.out.println("Introduce la nueva ruta completa (incluye el nombre):");
                        String destinoStr = sc.nextLine();
                        Files.move(origen.toPath(), Path.of(destinoStr), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Fichero movido correctamente.");
                        break;

                    case 3: // Copiar fichero
                        System.out.println("Introduce el nombre del fichero a copiar:");
                        String origenCopiaStr = sc.nextLine();
                        File origenCopia = new File(dir, origenCopiaStr);
                        if (!origenCopia.exists()) {
                            System.out.println("El fichero no existe.");
                            break;
                        }
                        System.out.println("Introduce la ruta de destino (incluye el nombre):");
                        String destinoCopiaStr = sc.nextLine();
                        Files.copy(origenCopia.toPath(), Path.of(destinoCopiaStr), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Fichero copiado correctamente.");
                        break;

                    case 4: // Borrar fichero
                        System.out.println("Introduce el nombre del fichero a borrar:");
                        String borrar = sc.nextLine();
                        File fBorrar = new File(dir, borrar);
                        if (fBorrar.delete()) {
                            System.out.println("Fichero borrado correctamente.");
                        } else {
                            System.out.println("No se pudo borrar el fichero.");
                        }
                        break;

                    case 5: // Salir
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 5);
    }

    // Metodo para mostrar los ficheros y directorios de la ruta dada
    private static void mostrarContenido(File dir) {
        System.out.println("\nContenido del directorio:");
        File[] archivos = dir.listFiles();
        if (archivos == null) {
            System.out.println("No se pudo acceder al contenido.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (File f : archivos) {
            if (f.isDirectory()) {
                System.out.println("[DIR] " + f.getName());
            } else {
                System.out.println("[FILE] " + f.getName() +
                        " | Tamaño: " + f.length() + " bytes" +
                        " | Última modificación: " + sdf.format(f.lastModified()));
            }
        }
    }
}