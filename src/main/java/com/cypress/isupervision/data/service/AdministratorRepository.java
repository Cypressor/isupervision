package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {
    Administrator findByUsername(String username);
    Administrator findByEmail(String email);
}