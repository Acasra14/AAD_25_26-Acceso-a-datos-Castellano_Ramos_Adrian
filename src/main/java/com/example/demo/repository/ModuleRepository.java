package com.example.demo.repository;

import com.example.demo.model.Module;
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
public class ModuleRepository {

    private final DataSource dataSource;

    private static final String SQL_INSERT = """
        INSERT INTO modulo (codigo, nombre, horas) 
        VALUES (?, ?, ?)
        """;
    private static final String SQL_FIND_ALL = "SELECT * FROM modulo";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM modulo WHERE id_modulo = ?";
    private static final String SQL_FIND_BY_CODE = "SELECT * FROM modulo WHERE codigo = ?";
    private static final String SQL_UPDATE = """
        UPDATE modulo SET codigo = ?, nombre = ?, horas = ? 
        WHERE id_modulo = ?
        """;
    private static final String SQL_DELETE = "DELETE FROM modulo WHERE id_modulo = ?";

    public Module insert(Module module) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        module.setId(rs.getInt(1));
                    }
                }
            }
            log.info("Module inserted: {}", module);
            return module;
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting module", e);
        }
    }

    public List<Module> findAll() {
        List<Module> modules = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modules.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all modules", e);
        }
        return modules;
    }

    public Module findById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding module by id: " + id, e);
        }
        return null;
    }

    public Module findByCode(String code) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_CODE)) {

            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding module by code: " + code, e);
        }
        return null;
    }

    public Module update(Module module) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());
            ps.setInt(4, module.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                log.info("Module updated: {}", module);
                return module;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating module", e);
        }
        return null;
    }

    public boolean delete(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            log.info("Module deleted with id: {}", id);
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting module", e);
        }
    }

    private Module mapRow(ResultSet rs) throws SQLException {
        return new Module(
                rs.getInt("id_modulo"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getInt("horas")
        );
    }
}