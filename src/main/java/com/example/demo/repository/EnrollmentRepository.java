package com.example.demo.repository;

import com.example.demo.model.Enrollment;
import com.example.demo.model.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EnrollmentRepository {

    private final DataSource dataSource;

    private static final String SQL_INSERT = """
        INSERT INTO matricula (id_alumno, id_modulo, fecha) 
        VALUES (?, ?, ?)
        """;
    private static final String SQL_FIND_ALL = "SELECT * FROM matricula";
    private static final String SQL_FIND_BY_STUDENT = "SELECT * FROM matricula WHERE id_alumno = ?";
    private static final String SQL_DELETE = "DELETE FROM matricula WHERE id_alumno = ? AND id_modulo = ?";
    private static final String SQL_COUNT_ENROLLMENTS = "{ ? = call count_enrollments(?) }";

    public Enrollment create(Enrollment enrollment) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, enrollment.getStudentId());
            ps.setInt(2, enrollment.getModuleId());
            ps.setDate(3, Date.valueOf(enrollment.getDate() != null ? enrollment.getDate() : LocalDate.now()));

            ps.executeUpdate();
            log.info("Enrollment created: {}", enrollment);
            return enrollment;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating enrollment", e);
        }
    }

    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                enrollments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all enrollments", e);
        }
        return enrollments;
    }

    public List<Enrollment> findByStudent(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_STUDENT)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding enrollments by student: " + studentId, e);
        }
        return enrollments;
    }

    public boolean delete(int studentId, int moduleId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, studentId);
            ps.setInt(2, moduleId);
            int affectedRows = ps.executeUpdate();
            log.info("Enrollment deleted for student {} and module {}", studentId, moduleId);
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting enrollment", e);
        }
    }

    public int countEnrollments(int studentId) {
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(SQL_COUNT_ENROLLMENTS)) {

            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, studentId);
            cs.execute();
            int total = cs.getInt(1);
            log.info("Total enrollments for student {}: {}", studentId, total);
            return total;
        } catch (SQLException e) {
            throw new RuntimeException("Error counting enrollments", e);
        }
    }

    public void createEnrollment(Enrollment enrollment, List<Module> modules) {
        // This method will be called within a transaction managed by StudentManagementService
        create(enrollment);
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        return new Enrollment(
                null, // id is not in the primary key in the schema
                rs.getInt("id_alumno"),
                rs.getInt("id_modulo"),
                rs.getDate("fecha").toLocalDate()
        );
    }
}