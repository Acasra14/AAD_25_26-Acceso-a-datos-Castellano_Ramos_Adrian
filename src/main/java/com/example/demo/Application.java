package com.example.demo;

import com.example.demo.model.Enrollment;
import com.example.demo.model.Profile;
import com.example.demo.service.ManagementService;
import com.example.demo.model.Module;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final ManagementService ManagementService;
    private final StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Profile perfil = new Profile();
        perfil.setAddress("Calle Falsa 123");
        perfil.setPhone("600112233");
        Student miriam = new Student();
        miriam.setNif("66280457T");
        miriam.setName("Miriam");
        miriam.setEmail("miriam@g.educaand.es");
        miriam.setCourse("DAW");
        miriam.setProfile(perfil);
        Module programacion = new Module();
        programacion.setCode("0485");
        programacion.setName("Programación");
        programacion.setHours(250);
        miriam = ManagementService.createStudent(miriam);
        log.info("Alumno creado: {}", miriam);
        programacion = ManagementService.createModule(programacion);
        log.info("Módulo creado: {}", programacion);
        Enrollment enrollment = ManagementService.enrollStudentInModule(miriam.getId(),
                programacion.getId());
        log.info("Matrícula realizada: {}", enrollment);
        int countEnrollments = ManagementService.countEnrollments(miriam.getId());
        log.info("{} módulos matriculados para el alumno {}", countEnrollments, miriam.getName());
        miriam = studentRepository.findByNif(miriam.getNif()).orElseThrow(() -> new
                RuntimeException("Alumno no encontrado"));
        log.info("Alumno recuperado: {}", miriam);
// studentRepository.delete(miriam);
// log.info("Alumno {} eliminado", miriam.getName());
// throw new RuntimeException("Forzando rollback de la transacción");
    }
}