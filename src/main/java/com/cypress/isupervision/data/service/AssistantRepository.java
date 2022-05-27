package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Assistant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistantRepository extends JpaRepository<Assistant, UUID> {

}