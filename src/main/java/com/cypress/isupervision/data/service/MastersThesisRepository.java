package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import java.util.UUID;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MastersThesisRepository extends JpaRepository<MastersThesis, UUID> {

}