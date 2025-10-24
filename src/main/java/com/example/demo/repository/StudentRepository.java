package com.example.demo.repository;

import com.example.demo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class StudentRepository implements CrudRepository<Student> {


    /**
     * @param entity
     * @return
     */
    @Override
    public Student create(Student entity) {
        log.info("insert: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Student read(Student entity) {
        log.info("read: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Student update(Student entity) {
        log.info("update: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean delete(Student entity) {
        log.info("delete: {}", entity.toString());
        return false;
    }
}
