package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.Assistent;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssistentService {

    private final AssistentRepository repository;

    @Autowired
    public AssistentService(AssistentRepository repository) {
        this.repository = repository;
    }

    public Optional<Assistent> get(UUID id) {
        return repository.findById(id);
    }

    public Assistent update(Assistent entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Assistent> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
