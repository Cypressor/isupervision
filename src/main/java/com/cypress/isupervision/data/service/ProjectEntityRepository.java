package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProjectEntityRepository extends JpaRepository<ProjectEntity, UUID> {

}