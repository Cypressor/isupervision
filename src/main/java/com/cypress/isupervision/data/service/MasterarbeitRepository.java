package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.Masterarbeit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterarbeitRepository extends JpaRepository<Masterarbeit, UUID> {

}