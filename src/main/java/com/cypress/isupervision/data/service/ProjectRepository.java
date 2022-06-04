/*
 * iSupervision
 * ProjectRepository
 * Repository, responsible for Project objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.Project;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Project findByTitle(String title);

    @Query("select project from Project project " +
            "where (project.assistant) like :searchTerm")
    List<Project> searchForAssistant(@Param("searchTerm") String searchTerm);

    @Query("select project from Project project " +
            "where (project.student) like :searchTerm")
    List<Project> searchForStudent(@Param("searchTerm") String searchTerm);

    @Query("select project from Project project " +
            "where ((project.student) is null or (project.student) = '') and (project.isFinished) = false")
    List<Project> searchOpenProjects();
}