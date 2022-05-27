package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import java.util.Optional;
import java.util.UUID;
import com.cypress.isupervision.data.entity.project.MastersThesis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MastersThesisService {

    private final MastersThesisRepository repository;

    @Autowired
    public MastersThesisService(MastersThesisRepository repository) {
        this.repository = repository;
    }

    public Optional<MastersThesis> get(UUID id) {
        return repository.findById(id);
    }

    public MastersThesis update(MastersThesis entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<MastersThesis> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
