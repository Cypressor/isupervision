package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.Projekt;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjektService {

    private final ProjektRepository repository;

    @Autowired
    public ProjektService(ProjektRepository repository) {
        this.repository = repository;
    }

    public Optional<Projekt> get(UUID id) {
        return repository.findById(id);
    }

    public Projekt update(Projekt entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Projekt> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
