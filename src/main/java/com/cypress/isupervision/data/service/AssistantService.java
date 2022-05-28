package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.Assistant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {

    private final AssistantRepository repository;

    @Autowired
    public AssistantService(AssistantRepository repository) {
        this.repository = repository;
    }

    public Assistant get(String username) { return repository.findByUsername(username); }

    public Optional<Assistant> get(UUID id) {
        return repository.findById(id);
    }

    public Assistant update(Assistant entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Assistant> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
