package com.company.store.controller;

import com.company.store.model.LoggedInUser;
import com.company.store.service.LoginService;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@Controller
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private LoginService loginService;
    @Autowired
    private LoggedInUser loggedInUser;


    @GetMapping("/")
    public String defaultPage(Model model){
        return "redirect:/menu";
    }

    @GetMapping("sessionId")
    public @ResponseBody String getSessionId(){
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        LOGGER.info("SessionID: " + sessionId);
        return sessionId;
    }

    @GetMapping("menu")
    public String getMenuPage(Model model){
        LOGGER.info("USER LOGGED IN: " + loggedInUser.isUserLoggedIn());
        model.addAttribute("isUserLoggedIn", loggedInUser.isUserLoggedIn());

        return "menu";
    }

    @GetMapping("login")
    public String userLogin(){
        // if user logged in
        if(loggedInUser.isUserLoggedIn()){
            return "userAlreadyLoggedIn";
        }

        return "login";
    }

    @PostMapping("login")
    public ModelAndView userLogin(
            @RequestParam(value="email") String email,
            @RequestParam(value="password") String password,
            Model model
    ){
        Triplet<String, Model, HttpStatus> modelViewTriplet = loginService.logInUser(email, password, model);
        model = modelViewTriplet.getValue1();

        ModelAndView mv = new ModelAndView();
        mv.setViewName(modelViewTriplet.getValue0());
        mv.setStatus(modelViewTriplet.getValue2());

        // return view
        return mv;
    }

    @PostMapping("logout")
    public String userLogout(){
        // if user didn't log in
        if(!loggedInUser.isUserLoggedIn()){
            return "loginRequired";
        }

        loggedInUser = null;
        // redirect to login page
        return "login";
    }

    @GetMapping("change-password")
    public ModelAndView showChangePasswordForm(){
        ModelAndView mv = new ModelAndView();
        // if user didn't log in
        if(!loggedInUser.isUserLoggedIn()){
            mv.setViewName("loginRequired");
            mv.setStatus(HttpStatus.UNAUTHORIZED);
        } else {
            mv.setViewName("changePassword");
            mv.setStatus(HttpStatus.OK);
        }
        return mv;
    }

    @PostMapping("change-password")
    public ModelAndView changePassword(@RequestParam String currentPassword,
                                       @RequestParam String newPassword,
                                       @RequestParam String repeatNewPassword,
                                       Model model) {
        Pair<ModelAndView, Model> modelViewPair = loginService.changePassword(currentPassword, newPassword, repeatNewPassword, model);
        // return view
        return modelViewPair.getFirst();
    }
}
