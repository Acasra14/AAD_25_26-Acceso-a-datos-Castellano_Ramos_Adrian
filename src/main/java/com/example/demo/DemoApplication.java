package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream("imagen.jpg"))) {

            byte[] buffer = new byte[1024];
            int bytesLeidos;
            int total = 0;

            while ((bytesLeidos = bis.read(buffer)) != -1) {
                total += bytesLeidos;
            }
            System.out.println("Imagen leída con éxito. Total bytes: " + total);
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
        try {
            BufferedImage img = ImageIO.read(new File("imagen.jpg"));
            // Escalar la imagen para que quepa en consola
            int newWidth = 100; // ancho en caracteres
            int newHeight = (img.getHeight() * newWidth) / img.getWidth();
            BufferedImage scaled = new BufferedImage(newWidth, newHeight,
                    BufferedImage.TYPE_INT_RGB);
            scaled.getGraphics().drawImage(img, 0, 0, newWidth, newHeight, null);
            // Gradiente de caracteres de más oscuro a más claro
            String gradient = "@#8&xo;:,. ";
            for (int y = 0; y < newHeight; y += 2) { // saltamos filas para corregir proporción
                for (int x = 0; x < newWidth; x++) {
                    Color c = new Color(scaled.getRGB(x, y));
                    int gris = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                    int index = (gris * (gradient.length() - 1)) / 255;
                    System.out.print(gradient.charAt(index));
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }

    }
}
