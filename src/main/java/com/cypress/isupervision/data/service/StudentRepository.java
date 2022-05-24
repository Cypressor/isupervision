package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.Student;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {

}