/*
 * iSupervision
 * BachelorsThesisService
 * Serviceclass, responsible for BachelorThesis objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.BachelorsThesis;
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
public class BachelorsThesisService {

    private final BachelorsThesisRepository repository;

    @Autowired
    public BachelorsThesisService(BachelorsThesisRepository repository) {
        this.repository = repository;
    }

    public Optional<BachelorsThesis> get(UUID id) {
        return repository.findById(id);
    }

    public BachelorsThesis update(BachelorsThesis entity) {
        entity.setTitle(entity.getTitle().trim());
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<BachelorsThesis> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<BachelorsThesis> searchForAssistant(Assistant assistant) {return repository.searchForAssistant(assistant);}

    public List<BachelorsThesis> searchForStudent(Student student) {return repository.searchForStudent(student);}

    public List<BachelorsThesis> searchOpenProjects() {return repository.searchOpenProjects();}

    public int count() {
        return (int) repository.count();
    }

    public int exists(BachelorsThesis bachelorsThesis)
    {
        int exists=0;
        BachelorsThesis tempBachelorsThesis = repository.findByTitle(bachelorsThesis.getTitle());
        if (tempBachelorsThesis != null)
        {
            exists+=1;
        }
        return exists;
    }
}
