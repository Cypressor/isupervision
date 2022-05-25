package com.cypress.isupervision.data.service;

import com.cypress.isupervision.data.entity.user.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
    User findByEmail(String email);
}