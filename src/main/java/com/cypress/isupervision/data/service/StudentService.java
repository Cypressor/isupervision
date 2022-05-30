package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Student;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentService
{

    private final StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Optional<Student> get(UUID id) {
        return repository.findById(id);
    }

    public Student get(String username) { return repository.findByUsername(username); }

    public Student update(Student entity) {
        entity.setUsername(entity.getUsername().trim());
        entity.setFirstname(entity.getFirstname().trim());
        entity.setLastname(entity.getLastname().trim());
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

    public int exists(Student student)
    {
        int exists=0;

        Student tempStudent = repository.findByUsername(student.getUsername());
        if (tempStudent != null)
        {
            exists+=1;
        }
        tempStudent = repository.findByEmail(student.getEmail());
        if (tempStudent!=null)
        {
            exists+=2;
        }
        return exists;
    }

}
