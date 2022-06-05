/*
 * iSupervision
 * MastersThesisService
 * Serviceclass, responsible for MastersThesis objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        entity.setTitle(entity.getTitle().trim());
        entity.setStudent(entity.getStudent().trim());
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<MastersThesis> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<MastersThesis> searchForAssistant(String assistantName) {return repository.searchForAssistant(assistantName);}
    public List<MastersThesis> searchForStudent(String studentName) {return repository.searchForStudent(studentName);}

    public List<MastersThesis> searchOpenProjects() {return repository.searchOpenProjects();}

    public int count() {
        return (int) repository.count();
    }

    public int exists(MastersThesis mastersThesis)
    {
        int exists=0;

        MastersThesis tempMastersThesis = repository.findByTitle(mastersThesis.getTitle());
        if (tempMastersThesis!= null)
        {
            exists+=1;
        }
        return exists;
    }
}
