/*
 * iSupervision
 * ProjectService
 * Serviceclass, responsible for Project objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.Project;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.entity.user.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectService  {

    private final ProjectRepository repository;

    @Autowired
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public Optional<Project> get(UUID id) {
        return repository.findById(id);
    }

    public Project get(String title) { return repository.findByTitle(title);}

    public Project update(Project entity) {
        entity.setTitle(entity.getTitle().trim());
        return repository.save(entity);
    }

    public List<Project> searchForAssistant(Assistant assistant) {return repository.searchForAssistant(assistant);}

    public List<Project> searchForStudent(Student student) {return repository.searchForStudent(student);}

    public List<Project> searchOpenProjects() {return repository.searchOpenProjects();}

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Project> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public int exists(Project project)
    {
        int exists=0;

        Project tempProject = repository.findByTitle(project.getTitle());
        if (tempProject != null)
        {
            exists+=1;
        }
        return exists;
    }

}
