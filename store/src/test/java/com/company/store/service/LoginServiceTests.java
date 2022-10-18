package com.company.store.service;

import com.company.store.DatabaseConfig;
import com.company.store.model.*;
import com.company.store.repository.UserRepositoryTests;
import org.javatuples.Triplet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class LoginServiceTests {

    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class.getName());
    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Autowired
    private LoginService loginService;
    @Autowired
    private LoggedInUser loggedInUser;
    @Autowired
    private UserService userService;
    @Mock
    private Model model;



    @BeforeEach
    public void setUp(){
        databaseConfig.fillTestDatabase();
    }

    @AfterEach
    public void teardown(){
        try {
            databaseConfig.cleanTestDatabase(databaseConfig.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void contextLoads() {
        assertNotNull(loginService, "LoginService is null.");
        assertNotNull(loggedInUser, "LoggedInUser is null.");
        assertNotNull(userService, "UserService is null.");
        assertNotNull(model, "Model is null.");
    }


    @Test
    public void whenLoginWithCorrectCredentials_thenReturnProperViewMessageHttpStatus(){
        // given
        String emailCorrect = "my@email.com";
        String passwordCorrect = "123";

        // when
        Triplet<String, Model, HttpStatus> triplet = loginService.logInUser(emailCorrect, passwordCorrect, model);

        // then
        String viewName = triplet.getValue0();
        Model returnedModel = triplet.getValue1();
        HttpStatus httpStatus = triplet.getValue2();

        assertEquals("menu", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.ACCEPTED, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenLoginWithIncorrectCredentials_thenReturnProperViewMessageHttpStatus(){
        // given
        String emailIncorrect = "unknown";
        String passwordIncorrect = "zxczxc123123";

        // when
        Triplet<String, Model, HttpStatus> triplet = loginService.logInUser(emailIncorrect, passwordIncorrect, model);

        // then
        String viewName = triplet.getValue0();
        Model returnedModel = triplet.getValue1();
        HttpStatus httpStatus = triplet.getValue2();

        verify(returnedModel, times(1)).addAttribute("incorrectCredentials", "Entered credentials are incorrect. Try again.");
        assertEquals("login", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.UNAUTHORIZED, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenChangePasswordIncorrectActualPassword_thenReturnProperViewMessageHttpStatus(){
        // given
        User user = userService.findById(1);
        loggedInUser.setLoggedUser(user);
        String currentPassword = "incorrectPassword";
        String newPassword = "abc123";
        String repeatNewPassword = "abc123";

        // when
        Pair<ModelAndView, Model> modelAndViewModelPair = loginService.changePassword(currentPassword, newPassword, repeatNewPassword, model);

        // then
        ModelAndView  mv = modelAndViewModelPair.getFirst();
        String viewName = mv.getViewName();
        Model returnedModel = modelAndViewModelPair.getSecond();
        HttpStatus httpStatus = mv.getStatus();

        verify(returnedModel, times(1)).addAttribute("incorrectInput", "Unfortunately, provided data is incorrect. Try again.");
        assertEquals("changePassword", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.CONFLICT, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenChangePasswordNewPasswordNotMatch_thenReturnProperViewMessageHttpStatus(){
        // given
        User user = userService.findById(1);
        loggedInUser.setLoggedUser(user);
        String currentPassword = "123";
        String newPassword = "abc123";
        String repeatNewPassword = "abc";

        // when
        Pair<ModelAndView, Model> modelAndViewModelPair = loginService.changePassword(currentPassword, newPassword, repeatNewPassword, model);

        // then
        ModelAndView  mv = modelAndViewModelPair.getFirst();
        String viewName = mv.getViewName();
        Model returnedModel = modelAndViewModelPair.getSecond();
        HttpStatus httpStatus = mv.getStatus();

        verify(returnedModel, times(1)).addAttribute("incorrectInput", "Unfortunately, provided data is incorrect. Try again.");
        assertEquals("changePassword", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.CONFLICT, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenChangePasswordCorrectInput_thenReturnProperViewMessageHttpStatus(){
        // given
        User user = userService.findById(1);
        loggedInUser.setLoggedUser(user);
        String currentPassword = "123";
        String newPassword = "abc123";
        String repeatNewPassword = "abc123";

        // when
        Pair<ModelAndView, Model> modelAndViewModelPair = loginService.changePassword(currentPassword, newPassword, repeatNewPassword, model);

        // then
        ModelAndView  mv = modelAndViewModelPair.getFirst();
        String viewName = mv.getViewName();
        Model returnedModel = modelAndViewModelPair.getSecond();
        HttpStatus httpStatus = mv.getStatus();

        verify(returnedModel, times(1)).addAttribute("changePasswordAccept", "Success! Your password has been changed.");
        assertEquals("changePassword", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.ACCEPTED, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenUserNotLoggedInChangePassword_thenReturnProperViewMessageHttpStatus(){
        // given
        loggedInUser.logOut();
        String currentPassword = "123";
        String newPassword = "abc123";
        String repeatNewPassword = "abc123";

        // when
        Pair<ModelAndView, Model> modelAndViewModelPair = loginService.changePassword(currentPassword, newPassword, repeatNewPassword, model);

        // then
        ModelAndView  mv = modelAndViewModelPair.getFirst();
        String viewName = mv.getViewName();
        Model returnedModel = modelAndViewModelPair.getSecond();
        HttpStatus httpStatus = mv.getStatus();

        verify(returnedModel, times(1)).addAttribute("incorrectInput", "Unfortunately, provided data is incorrect. Try again.");
        assertEquals("changePassword", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.CONFLICT, httpStatus, "Incorrect http status returned.");
    }

}
