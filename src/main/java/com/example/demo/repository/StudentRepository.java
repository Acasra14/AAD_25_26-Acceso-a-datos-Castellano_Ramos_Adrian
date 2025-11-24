package com.example.demo.repository;

import com.example.demo.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StudentRepository {

    private final DataSource dataSource;

    private static final String SQL_INSERT = """
        INSERT INTO alumno (nif, nombre, email) 
        VALUES (?, ?, ?)
        """;
    private static final String SQL_FIND_ALL = "SELECT * FROM alumno";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM alumno WHERE id_alumno = ?";
    private static final String SQL_UPDATE = """
        UPDATE alumno SET nif = ?, nombre = ?, email = ? 
        WHERE id_alumno = ?
        """;
    private static final String SQL_DELETE = "DELETE FROM alumno WHERE id_alumno = ?";

    public Student insert(Student student) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setId(rs.getInt(1));
                    }
                }
            }
            log.info("Student inserted: {}", student);
            return student;
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting student", e);
        }
    }

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all students", e);
        }
        return students;
    }

    public Student findById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding student by id: " + id, e);
        }
        return null;
    }

    public Student update(Student student) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setInt(4, student.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                log.info("Student updated: {}", student);
                return student;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating student", e);
        }
        return null;
    }

    public boolean delete(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            log.info("Student deleted with id: {}", id);
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student", e);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id_alumno"),
                rs.getString("nif"),
                rs.getString("nombre"),
                rs.getString("email"),
                null,
                null
        );
    }
}