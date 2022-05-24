package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.Bachelorarbeit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BachelorarbeitRepository extends JpaRepository<Bachelorarbeit, UUID> {

}