package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Administrator;
import com.cypress.isupervision.data.entity.user.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdministratorService
{

    private final AdministratorRepository repository;

    @Autowired
    public AdministratorService(AdministratorRepository repository) {
        this.repository = repository;
    }

    public Optional<Administrator> get(UUID id) {
        return repository.findById(id);
    }

    public Administrator update(Administrator entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Administrator> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
