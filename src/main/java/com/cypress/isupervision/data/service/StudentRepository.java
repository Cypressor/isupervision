package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Student;
import java.util.UUID;

import com.cypress.isupervision.data.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Student findByUsername(String username);
    Student findByEmail(String email);
}