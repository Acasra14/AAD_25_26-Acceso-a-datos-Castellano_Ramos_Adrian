package com.example.demo.model;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Student extends Person {

    private String curso;
    private List<Modules> modules;

    public Student(String dni, String name, String surname) {
        super(dni, name, surname);
    }

    public Student(String dni, String name, String surname, String curso) {
        super(dni, name, surname);
        this.curso = curso;
    }

}
