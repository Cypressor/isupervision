package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.Assistent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistentRepository extends JpaRepository<Assistent, UUID> {

}