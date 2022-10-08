package com.company.store.service;

import com.company.store.model.User;
import com.company.store.repository.OrderRepository;
import com.company.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;


    @Override
    @Transactional
    public User addUser(User user){
        user = userRepository.save(user);
        return user;
    }

    @Override
    public boolean isUserPasswordCorrect(String userEmail, String passwordToCheck){
        return userRepository.isUserPasswordCorrect(userEmail, passwordToCheck);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(int Id) {
        return userRepository.findById(Id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


}
