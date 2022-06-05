/*
 * iSupervision
 * AssistantRepository
 * Repository, responsible for Assistant objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import com.cypress.isupervision.data.entity.user.Assistant;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssistantRepository extends JpaRepository<Assistant, UUID> {
    Assistant findByUsername(String username);
    Assistant findByEmail(String email);

}