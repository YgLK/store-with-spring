package com.company.store.service;

import com.company.store.controller.LoginController;
import com.company.store.model.LoggedInUser;
import com.company.store.model.User;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private LoggedInUser loggedInUser;

    @Override
    public Triplet<String, Model, HttpStatus> logInUser(String email, String password, Model model){
        // check if password is correct
        var isPasswordCorrect = userService.isUserPasswordCorrect(email, password);
        // if password isn't correct return login page
        if(!isPasswordCorrect){
            System.out.println("PASSWORD INCORRECT");
            model.addAttribute("incorrectCredentials", "Entered credentials are incorrect. Try again.");
            return Triplet.with("login", model, HttpStatus.UNAUTHORIZED);
        }

        // if password fits assign logged-in user and return menu page
        loggedInUser.setLoggedUser(userService.findByEmail(email));
        LOGGER.info("PASSWORD correct");

        model.addAttribute("isUserLoggedIn", loggedInUser.isUserLoggedIn());

        return Triplet.with("menu", model, HttpStatus.ACCEPTED);
    }


    @Override
    public Pair<ModelAndView, Model> changePassword(String currentPassword,
                                                    String newPassword,
                                                    String repeatNewPassword,
                                                    Model model)
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("changePassword");

        boolean isCurrentPasswordCorrect;
        if(!loggedInUser.isUserLoggedIn()){
            isCurrentPasswordCorrect = false;
        } else {
            // if user logged in
            isCurrentPasswordCorrect = userService.isUserPasswordCorrect(loggedInUser.getLoggedUser().getEmail(), currentPassword);
        }

        LOGGER.info("Is currentPassword correct: " + isCurrentPasswordCorrect);
        LOGGER.info("Are newPasswords equal: " + newPassword.equals(repeatNewPassword));

        if(isCurrentPasswordCorrect && newPassword.equals(repeatNewPassword)){
            // this method will update password property of User instance
            User loggedUser = loggedInUser.getLoggedUser();
            loggedUser.setPassword(newPassword);
            userService.addUser(loggedUser);
            model.addAttribute("changePasswordAccept", "Success! Your password has been changed.");
            mv.setStatus(HttpStatus.ACCEPTED);
        } else {
            model.addAttribute("incorrectInput", "Unfortunately, provided data is incorrect. Try again.");
            mv.setStatus(HttpStatus.CONFLICT);
        }
        return Pair.of(mv, model);
    }
}
