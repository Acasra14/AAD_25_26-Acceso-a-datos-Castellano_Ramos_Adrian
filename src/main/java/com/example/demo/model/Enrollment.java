package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    private Integer id;
    private Integer studentId;
    private Integer moduleId;
    private LocalDate date;
}