package com.example.demo.application;

import com.example.demo.config.PostgresqlDriver;
import com.example.demo.model.Enrollment;
import com.example.demo.model.Module;
import com.example.demo.model.Student;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.ModuleRepository;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentManagementService {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PostgresqlDriver postgresqlDriver;

    public Module createModule(Module module) {
        // Check if module with same code already exists
        Module existingModule = moduleRepository.findByCode(module.getCode());
        if (existingModule != null) {
            log.info("Module already exists with code: {}", module.getCode());
            return existingModule;
        }

        // Validate module data
        if (module.getCode() == null || module.getCode().trim().isEmpty() ||
                module.getName() == null || module.getName().trim().isEmpty() ||
                module.getHours() == null || module.getHours() <= 0) {
            throw new IllegalArgumentException("Invalid module data");
        }

        return moduleRepository.insert(module);
    }

    public Student createStudent(Student student) {
        // Validate student data
        if (!validate(student)) {
            throw new IllegalArgumentException("Invalid student data");
        }

        // Check if student with same NIF already exists
        List<Student> allStudents = studentRepository.findAll();
        boolean nifExists = allStudents.stream()
                .anyMatch(s -> s.getNif().equals(student.getNif()));

        if (nifExists) {
            throw new IllegalArgumentException("Student with NIF " + student.getNif() + " already exists");
        }

        return studentRepository.insert(student);
    }

    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        try {
            postgresqlDriver.beginTransaction();

            // Validate student exists
            Student student = studentRepository.findById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found: " + studentId);
            }

            // Validate module exists
            Module module = moduleRepository.findById(moduleId);
            if (module == null) {
                throw new IllegalArgumentException("Module not found: " + moduleId);
            }

            Enrollment enrollment = new Enrollment(null, studentId, moduleId, LocalDate.now());
            Enrollment created = enrollmentRepository.create(enrollment);

            postgresqlDriver.commit();
            log.info("Student {} successfully enrolled in module {}", studentId, moduleId);
            return created;

        } catch (Exception e) {
            log.error("Error enrolling student in module: {}", e.getMessage());
            postgresqlDriver.rollback();
            throw new RuntimeException("Error enrolling student in module: " + e.getMessage(), e);
        }
    }

    public boolean validate(Student student) {
        return student != null &&
                student.getNif() != null && !student.getNif().trim().isEmpty() &&
                student.getName() != null && !student.getName().trim().isEmpty() &&
                student.getEmail() != null && !student.getEmail().trim().isEmpty() &&
                student.getCurse() != null && !student.getCurse().trim().isEmpty();
    }

    public int getStudentEnrollmentCount(int studentId) {
        return enrollmentRepository.countEnrollments(studentId);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
}