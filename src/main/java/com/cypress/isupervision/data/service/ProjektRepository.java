package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.Projekt;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjektRepository extends JpaRepository<Projekt, UUID> {

}