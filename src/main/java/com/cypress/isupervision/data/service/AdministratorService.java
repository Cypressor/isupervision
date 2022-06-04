/*
 * iSupervision
 * AdminstratorService
 * Serviceclass, responsible for Administrator objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Administrator;
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

    public Administrator get(String username) {
        return repository.findByUsername(username); }

    public Administrator update(Administrator entity) {
        entity.setUsername(entity.getUsername().trim());
        entity.setFirstname(entity.getFirstname().trim());
        entity.setLastname(entity.getLastname().trim());
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
