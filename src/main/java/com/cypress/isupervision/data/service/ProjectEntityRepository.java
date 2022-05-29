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
            "where (projectEntity.assistant) like :searchTerm")
    List<ProjectEntity> searchForAssistant(@Param("searchTerm") String searchTerm);

    @Query("select projectEntity from ProjectEntity projectEntity " +
            "where (projectEntity.student) like :searchTerm")
    List<ProjectEntity> searchForStudent(@Param("searchTerm") String searchTerm);
}