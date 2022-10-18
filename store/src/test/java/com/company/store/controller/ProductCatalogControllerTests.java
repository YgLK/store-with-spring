package com.company.store.controller;


import com.company.store.DatabaseConfig;
import com.company.store.model.LoggedInUser;
import com.company.store.repository.UserRepositoryTests;
import com.company.store.service.ProductService;
import com.company.store.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductCatalogControllerTests {

    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class.getName());
    private final DatabaseConfig databaseConfig = new DatabaseConfig();


    @Autowired
    private LoggedInUser loggedInUser;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Mock
    private Model model;
     @Autowired
    private MockMvc mockMvc;


    @Before
    public void initMockMvc(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    public void setUp(){
        databaseConfig.fillTestDatabase();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
        assertNotNull(webApplicationContext, "WebApplicationContext is null.");
        assertNotNull(loggedInUser, "LoggedInUser is null.");
        assertNotNull(productService, "ProductService is null.");
        assertNotNull(model, "Model is null.");
        assertNotNull(mockMvc, "MockMvc is null.");
        assertNotNull(userService, "UserService is null.");
    }


    @Test
    public void whenUserNotLoggedIn_thenShowProductCatalog() throws Exception {
        // given
        loggedInUser.logOut();


        // then
        mockMvc
                .perform(MockMvcRequestBuilders.get("/product-catalog"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("productCatalog"));
    }
}
