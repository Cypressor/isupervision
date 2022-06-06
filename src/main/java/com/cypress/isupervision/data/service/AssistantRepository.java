/*
 * iSupervision
 * AssistantRepository
 * Repository, responsible for Assistant objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Assistant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AssistantRepository extends JpaRepository<Assistant, UUID> {
    Assistant findByUsername(String username);
    Assistant findByEmail(String email);

}