package com.cypress.isupervision.data.service;
import com.cypress.isupervision.data.entity.project.MastersThesis;
import java.util.List;
import java.util.UUID;

import com.cypress.isupervision.data.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MastersThesisRepository extends JpaRepository<MastersThesis, UUID> {
    MastersThesis findByTitle(String title);

    @Query("select mastersThesis from MastersThesis mastersThesis " +
            "where (mastersThesis.assistant) like :searchTerm")
    List<MastersThesis> searchForAssistant(@Param("searchTerm") String searchTerm);

    @Query("select mastersThesis from MastersThesis mastersThesis " +
            "where (mastersThesis.student) like :searchTerm")
    List<MastersThesis> searchForStudent(@Param("searchTerm") String searchTerm);

    @Query("select mastersThesis from MastersThesis mastersThesis " +
            "where (mastersThesis.student) is null or (mastersThesis.student) like :searchTerm")
    List<MastersThesis> searchOpenProjects(@Param("searchTerm") String searchTerm);
}