package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BachelorsThesisRepository extends JpaRepository<BachelorsThesis, UUID> {

}