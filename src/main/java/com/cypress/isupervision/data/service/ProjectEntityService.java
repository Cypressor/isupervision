/*
 * iSupervision
 * ProjectEntityService
 * Serviceclass, responsible for ProjectEntity objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.ProjectEntity;
import com.cypress.isupervision.data.entity.user.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
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

    public ProjectEntity get(String title) {
        return repository.findByTitle(title);
    }

    public ProjectEntity update(ProjectEntity entity) {
        entity.setTitle(entity.getTitle().trim());
        return repository.save(entity);}

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<ProjectEntity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<ProjectEntity> searchForAssistant(String assistantName) {return repository.searchForAssistant(assistantName);}

    public List<ProjectEntity> searchForStudent(Student student) {return repository.searchForStudent(student);}

    public int count() {
        return (int) repository.count();
    }

    public int exists(ProjectEntity projectEntity)
    {
        int exists=0;

        ProjectEntity tempProjectEntity = repository.findByTitle(projectEntity.getTitle());
        if (tempProjectEntity != null)
        {
            exists+=1;
        }
        return exists;
    }
}
