/*
 * iSupervision
 * MastersThesisRepository
 * Repository, responsible for MastersThesis objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import java.util.List;
import java.util.UUID;

import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MastersThesisRepository extends JpaRepository<MastersThesis, UUID> {
    MastersThesis findByTitle(String title);

    @Query("select mastersThesis from MastersThesis mastersThesis " +
            "where (mastersThesis.assistant) = :searchTerm")
    List<MastersThesis> searchForAssistant(@Param("searchTerm") Assistant searchTerm);

    @Query("select mastersThesis from MastersThesis mastersThesis " +
            "where (mastersThesis.student) = :searchTerm")
    List<MastersThesis> searchForStudent(@Param("searchTerm") Student searchTerm);

    @Query("select mastersThesis from MastersThesis mastersThesis " +
            "where ((mastersThesis.student) is null or (mastersThesis.student) = '') and (mastersThesis.isFinished) = false")
    List<MastersThesis> searchOpenProjects();
}