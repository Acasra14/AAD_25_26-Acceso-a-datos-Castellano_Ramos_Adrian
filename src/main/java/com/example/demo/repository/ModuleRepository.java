package com.example.demo.repository;

import com.example.demo.model.Module;
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
public class ModuleRepository {

    private final JdbcTemplate jdbcTemplate;

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

    private final RowMapper<Module> rowMapper = (rs, rowNum) -> new Module(
            rs.getInt("id_modulo"),
            rs.getString("codigo"),
            rs.getString("nombre"),
            rs.getInt("horas")
    );

    public Module insert(Module module) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());
            return ps;
        }, keyHolder);

        module.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Module inserted: {}", module);
        return module;
    }

    public List<Module> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rowMapper);
    }

    public Module findById(int id) {
        List<Module> modules = jdbcTemplate.query(SQL_FIND_BY_ID, rowMapper, id);
        return modules.isEmpty() ? null : modules.get(0);
    }

    public Module findByCode(String code) {
        List<Module> modules = jdbcTemplate.query(SQL_FIND_BY_CODE, rowMapper, code);
        return modules.isEmpty() ? null : modules.get(0);
    }

    public Module update(Module module) {
        int affectedRows = jdbcTemplate.update(SQL_UPDATE,
                module.getCode(),
                module.getName(),
                module.getHours(),
                module.getId());

        if (affectedRows > 0) {
            log.info("Module updated: {}", module);
            return module;
        }
        return null;
    }

    public boolean delete(int id) {
        int affectedRows = jdbcTemplate.update(SQL_DELETE, id);
        log.info("Module deleted with id: {}", id);
        return affectedRows > 0;
    }
}