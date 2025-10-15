package com.example.demo;

import java.util.Scanner;

public class MenuPrincipal {

    // Metodo principal
    public void ejecutarMenu() {
        // Crear Scanner para leer lo que escriba el usuario por teclado
        Scanner sc = new Scanner(System.in);
        int opcion = -1; // Variable para guardar la opción elegida

        // Bucle DO-WHILE: se ejecuta AL MENOS UNA VEZ y repite hasta que opcion sea 0
        do {
            try {
                // Mostrar el menú de opciones al usuario
                System.out.println("\n--- GESTIÓN DE ALUMNOS ---");
                System.out.println("1. Insertar nuevo alumno");
                System.out.println("2. Consultar alumno por posición");
                System.out.println("3. Modificar nota de alumno");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");

                // Leer lo que el usuario escribe
                String input = sc.nextLine();

                // Validación: si el usuario no escribió nada
                if (input.isEmpty()) {
                    System.out.println("Por favor, introduce una opción.");
                    continue; // Volver al inicio del bucle
                }

                // Convertir el texto a número
                opcion = Integer.parseInt(input);


                switch (opcion) {
                    case 1 -> {
                        // Crear objeto de AñadirAlumno y llamar a su metodo añadir()
                        AñadirAlumno añadir = new AñadirAlumno();
                        añadir.añadir();
                    }
                    case 2 -> {
                        // Crear objeto de ConsultarAlumno y llamar a su metodo consultar()
                        ConsultarAlumno consultar = new ConsultarAlumno();
                        consultar.consultar();
                    }
                    case 3 -> {
                        // Crear objeto de ModificarAlumno y llamar a su metodo modificar()
                        ModificarAlumno modificar = new ModificarAlumno();
                        modificar.modificar();
                    }
                    case 0 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida. Introduce un número del 0 al 3.");
                }
            }
            // ERRORES:
            catch (NumberFormatException e) {
                // Error si el usuario escribe algo que no es número
                System.out.println("Error: Debes introducir un número válido.");
            }
            catch (Exception e) {
                // Error para cualquier otro problema inesperado
                System.out.println("Error inesperado: " + e.getMessage());
            }
        } while (opcion != 0); // CONDICIÓN: repetir mientras no se elija SALIR

        sc.close();
        System.out.println("Programa terminado.");
    }
}