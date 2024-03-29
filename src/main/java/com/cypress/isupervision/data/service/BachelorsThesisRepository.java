/*
 * iSupervision
 * BachelorsThesisRepository
 * Repository, responsible for BachelorsThesis objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import java.util.List;
import java.util.UUID;

import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BachelorsThesisRepository extends JpaRepository<BachelorsThesis, UUID> {
    BachelorsThesis findByTitle(String title);

    @Query("select bachelorsThesis from BachelorsThesis bachelorsThesis " +
            "where (bachelorsThesis.assistant) = :searchTerm")
    List<BachelorsThesis> searchForAssistant(@Param("searchTerm") Assistant searchTerm);

    @Query("select bachelorsThesis from BachelorsThesis bachelorsThesis " +
            "where (bachelorsThesis.student) = :searchTerm")
    List<BachelorsThesis> searchForStudent(@Param("searchTerm") Student searchTerm);

    @Query("select bachelorsThesis from BachelorsThesis bachelorsThesis " +
            "where ((bachelorsThesis.student) is null or (bachelorsThesis.student) = '') and (bachelorsThesis.isFinished) = false")
    List<BachelorsThesis> searchOpenProjects();
}