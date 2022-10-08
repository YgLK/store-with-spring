package com.company.store.service;

import org.javatuples.Triplet;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public interface LoginService {
    Triplet<String, Model, HttpStatus> logInUser(String email, String password, Model model);

    Pair<ModelAndView, Model> changePassword(String currentPassword,
                                             String newPassword,
                                             String repeatNewPassword,
                                             Model model);
}
