package com.cypress.isupervision.data.service;


import com.cypress.isupervision.data.entity.project.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectEntityService
{

    private final ProjectEntityRepository repository;

    @Autowired
    public ProjectEntityService(ProjectEntityRepository repository) {
        this.repository = repository;
    }

    public Optional<ProjectEntity> get(UUID id) {
        return repository.findById(id);
    }

    public ProjectEntity update(ProjectEntity entity) {return repository.save(entity);}

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<ProjectEntity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
