/*
 * iSupervision
 * StudentRepository
 * Repository, responsible for Student objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Student;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Student findByUsername(String username);
    Student findByEmail(String email);
}