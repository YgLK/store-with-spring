package com.company.store.service;

import com.company.store.model.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService {
    @Transactional
    User addUser(User user);

    boolean isUserPasswordCorrect(String userEmail, String passwordToCheck);

    User findByEmail(String email);

    User findById(int Id);

    List<User> findAll();
}
