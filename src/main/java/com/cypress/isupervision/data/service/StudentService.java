package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Student;
import java.util.Optional;
import java.util.UUID;

import com.cypress.isupervision.data.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository repository;



    @Autowired
    public StudentService(StudentRepository repository) {
        this.repository = repository;

    }

    public Optional<Student> get(UUID id) {
        return repository.findById(id);
    }

    public Student update(Student entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Student> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
