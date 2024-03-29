/*
 * iSupervision
 * AssistantService
 * Serviceclass, responsible for Assistant objects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> get(UUID id) {
        return repository.findById(id);
    }

    public User update(User entity) {
        entity.setUsername(entity.getUsername().trim());
        entity.setFirstname(entity.getFirstname().trim());
        entity.setLastname(entity.getLastname().trim());
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public int exists(User user)
    {
        int exists=0;
        User tempUser = repository.findByUsername(user.getUsername());
        if (tempUser != null)
        {
            exists+=1;
        }
        tempUser = repository.findByEmail(user.getEmail());
        if (tempUser!=null)
        {
            exists+=2;
        }
        return exists;
    }
}
