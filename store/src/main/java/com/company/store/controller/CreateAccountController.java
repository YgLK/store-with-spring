package com.company.store.controller;

import com.company.store.model.User;
import com.company.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class CreateAccountController {

    @Autowired
    private UserService userService;


    @GetMapping("create-account")
    public String getCreateAccount(@ModelAttribute("user") User user){
        return "createNewAccount";
    }


    @PostMapping("create-account")
    public ModelAndView addNewAccount(@Valid @ModelAttribute("user") User user,
                                BindingResult result,
                                Model model
    ){
        // set default values of enabled and role (might be used in Spring Security)
        user.setEnabled(1);
        user.setRole("ROLE_USER");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("createNewAccount");

        if(result.hasErrors()){
            System.out.println("Failure while creating new account.");
            mv.setStatus(HttpStatus.BAD_REQUEST);
            return mv;
        }

        // check if User with chosen email already exists in database
        if(userService.findAll().stream().anyMatch(c -> Objects.equals(c.getEmail(), user.getEmail()))){
            mv.setStatus(HttpStatus.CONFLICT);
            model.addAttribute(
                    "accountAlreadyExists",
                    "Account associated with this email already exists.");
            return mv;
        }

        model.addAttribute(
                "accountCreateSuccess",
                "Account has been created successfully.");
        // save user from request (user is filled with the data entered with the use of html form)
        userService.addUser(user);

        return mv;
    }
}
