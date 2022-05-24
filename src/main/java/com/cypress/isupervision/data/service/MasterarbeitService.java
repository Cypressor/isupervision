package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.Masterarbeit;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MasterarbeitService {

    private final MasterarbeitRepository repository;

    @Autowired
    public MasterarbeitService(MasterarbeitRepository repository) {
        this.repository = repository;
    }

    public Optional<Masterarbeit> get(UUID id) {
        return repository.findById(id);
    }

    public Masterarbeit update(Masterarbeit entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Masterarbeit> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
