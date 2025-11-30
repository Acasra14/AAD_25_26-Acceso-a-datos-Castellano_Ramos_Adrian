package com.example.demo.repository;

import com.example.demo.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT = """
        INSERT INTO alumno (nif, nombre, email, curso) 
        VALUES (?, ?, ?, ?)
        """;
    private static final String SQL_FIND_ALL = "SELECT * FROM alumno";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM alumno WHERE id_alumno = ?";
    private static final String SQL_UPDATE = """
        UPDATE alumno SET nif = ?, nombre = ?, email = ?, curso = ? 
        WHERE id_alumno = ?
        """;
    private static final String SQL_DELETE = "DELETE FROM alumno WHERE id_alumno = ?";

    private final RowMapper<Student> rowMapper = (rs, rowNum) -> new Student(
            rs.getInt("id_alumno"),
            rs.getString("nif"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("curso"),
            null
    );

    public Student insert(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getCurse());
            return ps;
        }, keyHolder);

        student.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Student inserted: {}", student);
        return student;
    }

    public List<Student> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rowMapper);
    }

    public Student findById(int id) {
        List<Student> students = jdbcTemplate.query(SQL_FIND_BY_ID, rowMapper, id);
        return students.isEmpty() ? null : students.get(0);
    }

    public Student update(Student student) {
        int affectedRows = jdbcTemplate.update(SQL_UPDATE,
                student.getNif(),
                student.getName(),
                student.getEmail(),
                student.getCurse(),
                student.getId());

        if (affectedRows > 0) {
            log.info("Student updated: {}", student);
            return student;
        }
        return null;
    }

    public boolean delete(int id) {
        int affectedRows = jdbcTemplate.update(SQL_DELETE, id);
        log.info("Student deleted with id: {}", id);
        return affectedRows > 0;
    }
}