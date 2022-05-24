package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.Bachelorarbeit;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BachelorarbeitService {

    private final BachelorarbeitRepository repository;

    @Autowired
    public BachelorarbeitService(BachelorarbeitRepository repository) {
        this.repository = repository;
    }

    public Optional<Bachelorarbeit> get(UUID id) {
        return repository.findById(id);
    }

    public Bachelorarbeit update(Bachelorarbeit entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Bachelorarbeit> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
