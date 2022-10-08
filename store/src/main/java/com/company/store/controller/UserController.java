package com.company.store.controller;


import com.company.store.model.User;
import com.company.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // create new account with post request
    @PostMapping("user")
    public @ResponseBody ResponseEntity<User> postUser(User user){
        // if user with entered email already exists
        if(userService.findAll().stream().anyMatch(c -> Objects.equals(c.getEmail(), user.getEmail()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new User());
        } else {
            // create new account
            System.out.println("User firstname: " + user.getFirstname());
            userService.addUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    @PostMapping("user/update")
    public @ResponseBody User updateUser(@Valid @ModelAttribute("user") User user,
                                         BindingResult result){
        return userService.addUser(user);
    }

    @GetMapping("user/{id}")
    public User findUserById(@PathVariable String id){
        return userService.findById(Integer.parseInt(id));
    }

    @GetMapping("users")
    public List<User> findAll() {
        return userService.findAll();
    }
}
