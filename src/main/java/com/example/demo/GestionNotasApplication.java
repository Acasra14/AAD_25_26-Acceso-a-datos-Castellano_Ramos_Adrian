package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionNotasApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GestionNotasApplication.class, args);
    }

    @Override
    public void run(String... args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.ejecutarMenu();
    }
}