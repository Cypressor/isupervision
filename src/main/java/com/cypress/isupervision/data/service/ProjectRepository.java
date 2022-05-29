package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.Project;
import java.util.UUID;

import com.cypress.isupervision.data.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Project findByTitle(String title);
}