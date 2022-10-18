package com.company.store.model;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class LoggedInUser {

    private User loggedUser = null;

    public void logOut(){
        loggedUser  = null;
    }

    public boolean isUserLoggedIn(){
        return loggedUser != null;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

}
