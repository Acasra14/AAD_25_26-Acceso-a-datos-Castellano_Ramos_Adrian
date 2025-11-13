package com.example.demo.application;

public interface CustomService<T> {

    boolean validate(T entity);

}
