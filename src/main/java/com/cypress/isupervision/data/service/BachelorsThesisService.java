package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BachelorsThesisService {

    private final BachelorsThesisRepository repository;

    @Autowired
    public BachelorsThesisService(BachelorsThesisRepository repository) {
        this.repository = repository;
    }

    public Optional<BachelorsThesis> get(UUID id) {
        return repository.findById(id);
    }

    public BachelorsThesis update(BachelorsThesis entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<BachelorsThesis> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
