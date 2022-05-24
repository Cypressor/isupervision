package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Assistent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistentRepository extends JpaRepository<Assistent, UUID> {

}