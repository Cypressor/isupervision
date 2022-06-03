package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.cypress.isupervision.data.entity.project.MastersThesis;
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
        entity.setAssistant(entity.getAssistant().trim());
        entity.setStudent(entity.getStudent().trim());
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<BachelorsThesis> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<BachelorsThesis> searchForAssistant(String assistantName) {return repository.searchForAssistant(assistantName);}

    public List<BachelorsThesis> searchForStudent(String studentName) {return repository.searchForStudent(studentName);}

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
