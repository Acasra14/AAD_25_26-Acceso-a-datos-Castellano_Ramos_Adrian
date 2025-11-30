package com.example.demo.repository;

import com.example.demo.model.Enrollment;
import com.example.demo.model.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EnrollmentRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT = """
        INSERT INTO matricula (id_alumno, id_modulo, fecha) 
        VALUES (?, ?, ?)
        """;
    private static final String SQL_FIND_ALL = "SELECT * FROM matricula";
    private static final String SQL_FIND_BY_STUDENT = "SELECT * FROM matricula WHERE id_alumno = ?";
    private static final String SQL_DELETE = "DELETE FROM matricula WHERE id_alumno = ? AND id_modulo = ?";

    private final RowMapper<Enrollment> rowMapper = (rs, rowNum) -> new Enrollment(
            null,
            rs.getInt("id_alumno"),
            rs.getInt("id_modulo"),
            rs.getDate("fecha").toLocalDate()
    );

    public Enrollment create(Enrollment enrollment) {
        jdbcTemplate.update(SQL_INSERT,
                enrollment.getStudentId(),
                enrollment.getModuleId(),
                enrollment.getDate() != null ? enrollment.getDate() : LocalDate.now());

        log.info("Enrollment created: {}", enrollment);
        return enrollment;
    }

    public List<Enrollment> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rowMapper);
    }

    public List<Enrollment> findByStudent(int studentId) {
        return jdbcTemplate.query(SQL_FIND_BY_STUDENT, rowMapper, studentId);
    }

    public boolean delete(int studentId, int moduleId) {
        int affectedRows = jdbcTemplate.update(SQL_DELETE, studentId, moduleId);
        log.info("Enrollment deleted for student {} and module {}", studentId, moduleId);
        return affectedRows > 0;
    }

    public int countEnrollments(int studentId) {
        SimpleJdbcCall countEnrollmentsCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("count_enrollments");

        Integer result = countEnrollmentsCall.executeFunction(Integer.class, studentId);
        int total = result != null ? result : 0;

        log.info("Total enrollments for student {}: {}", studentId, total);
        return total;
    }

    public void createEnrollment(Enrollment enrollment, List<Module> modules) {
        create(enrollment);
    }
}