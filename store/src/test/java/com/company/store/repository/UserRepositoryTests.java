package com.company.store.repository;

import com.company.store.DatabaseConfig;
import com.company.store.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserRepositoryTests {

    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class.getName());

    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private PasswordEncoder encoder;


    @BeforeEach
    public void setUp(){
        System.out.println("Before setup: "  + userRepository.findAll().size());
        databaseConfig.fillTestDatabase();
        System.out.println("After setup: "  + userRepository.findAll().size());
    }

    @AfterEach
    public void teardown(){
        System.out.println("Before delete: "  + userRepository.findAll().size());

        try {
            databaseConfig.cleanTestDatabase(databaseConfig.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("After delete: "  + userRepository.findAll().size());
    }

    @Test
    public void contextLoads() {
        assertNotNull(userRepository, "ProductRepository is null.");
        assertNotNull(encoder, "Encoder is null.");
    }

    @Test
    public void whenSaveUser_thenAddUserToDB(){
        // given
        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@gmail.com";
        String password = "password123";

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("USER");
        user.setEnabled(1);

        int beforeAddingUserCount = userRepository.findAll().size();
        LOGGER.log(Level.FINE, "Size of user table before: " + userRepository.findAll().size());

        // when
        user = userRepository.save(user);


        LOGGER.log(Level.FINE, "Size of user table after: " + userRepository.findAll().size());
        // then
        assertEquals(userRepository.findAll().size(), user.getId(), "User ID should equal the number of users in the table.");
        assertEquals(beforeAddingUserCount + 1,  userRepository.findAll().size(), "User table length should increment by 1");
    }

    @Test
    public void whenSaveUser_thenUserSavedPasswordEncrypted(){
        // given
        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@gmail.com";
        String password = "password123";

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("USER");
        user.setEnabled(1);

        // when
        user = userRepository.save(user);

        // then
        assertTrue(encoder.matches(password, user.getPassword()), "Encrypted password doesn't match user's password.");
    }

    @Test
    public void whenFindExistingUserByEmail_GetUserWithProperEmail(){
        //given
        String email = "my@email.com";

        // when
        User user = userRepository.findByEmail(email);

        // then
        assertTrue(encoder.matches("123", user.getPassword()), "User's password doesn't match.");
        assertEquals("Greg", user.getFirstname(), "User's first name doesn't match.");
        assertEquals("Josh", user.getLastname(), "User's last name doesn't match.");
    }

    @Test
    public void whenFindUserByCorrectId_GetUserWithProperId(){
        //given
        int id = 2;

        // when
        User user = userRepository.findById(id);

        // then
        assertTrue(encoder.matches("car123", user.getPassword()), "User's password doesn't match.");
        assertEquals("admin", user.getEmail(), "User's email doesn't match.");
        assertEquals("Carl", user.getFirstname(), "User's first name doesn't match.");
        assertEquals("Johnson", user.getLastname(), "User's last name doesn't match.");
    }

    @Test
    public void whenFindAllUsers_getAllUsersList(){
        // given

        // when
        List<User> users = userRepository.findAll();

        // then
        assertEquals(2, users.size(), "There should be two users in the list.");
        for(var user : users){
            assertNotNull(user.getId(), "User ID is null.");
            assertNotNull(user.getEmail(), "User email is null.");
            assertNotNull(user.getFirstname(), "User first name is null.");
            assertNotNull(user.getLastname(), "User last name is null.");
        }
    }

    @Test
    public void whenEnterIncorrectPassword_ReturnUserPasswordIsNotCorrect(){
        // given
        String incorrectPassword = "incorrectPassword";
        User user = userRepository.findById(1);

        // when
        boolean doesPasswordMatch = userRepository.isUserPasswordCorrect(user.getEmail(), incorrectPassword);

        // then
        assertFalse(doesPasswordMatch, "Incorrect password shouldn't be accepted.");
    }

    @Test
    public void whenEnterCorrectPassword_ReturnUserPasswordIsCorrect(){
        // given
        String correctPassword = "123";
        User user = userRepository.findById(1);

        // when
        boolean doesPasswordMatch = userRepository.isUserPasswordCorrect(user.getEmail(), correctPassword);

        // then
        assertTrue(doesPasswordMatch, "Correct password should be accepted.");
    }

}
