/*
 * iSupervision
 * AdministratorRepository
 * Repository, responsible for Administrator objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {
    Administrator findByUsername(String username);
    Administrator findByEmail(String email);
}