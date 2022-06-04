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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BachelorsThesisRepository extends JpaRepository<BachelorsThesis, UUID> {
    BachelorsThesis findByTitle(String title);

    @Query("select bachelorsThesis from BachelorsThesis bachelorsThesis " +
            "where (bachelorsThesis.assistant) like :searchTerm")
    List<BachelorsThesis> searchForAssistant(@Param("searchTerm") String searchTerm);
    @Query("select bachelorsThesis from BachelorsThesis bachelorsThesis " +
            "where (bachelorsThesis.student) like :searchTerm")
    List<BachelorsThesis> searchForStudent(@Param("searchTerm") String searchTerm);

    @Query("select bachelorsThesis from BachelorsThesis bachelorsThesis " +
            "where ((bachelorsThesis.student) is null or (bachelorsThesis.student) = '') and (bachelorsThesis.isFinished) = false")
    List<BachelorsThesis> searchOpenProjects();
}