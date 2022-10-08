package com.company.store.repository;

import com.company.store.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    User save(User user);

    User findByEmail(String email);

    User findById(int Id);

    List<User> findAll();

    boolean isUserPasswordCorrect(String userEmail, String passwordToCheck);
}
