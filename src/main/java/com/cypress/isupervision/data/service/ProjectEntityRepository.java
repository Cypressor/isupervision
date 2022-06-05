/*
 * iSupervision
 * ProjectEntityRepository
 * Repository, responsible for ProjectEntity objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ProjectEntityRepository extends JpaRepository<ProjectEntity, UUID> {
    ProjectEntity findByTitle(String title);

    @Query("select projectEntity from ProjectEntity projectEntity " +
            "where (projectEntity.assistant) = :searchTerm")
    List<ProjectEntity> searchForAssistant(@Param("searchTerm") String searchTerm);

    @Query("select projectEntity from ProjectEntity projectEntity " +
            "where (projectEntity.student) = :searchTerm")
    List<ProjectEntity> searchForStudent(@Param("searchTerm") String searchTerm);
}