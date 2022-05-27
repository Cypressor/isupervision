package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.Project;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

}