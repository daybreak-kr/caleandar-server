package com.daybreak.cleandar.domain.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    private final UserRepository userRepository;
    private final MockMvc mockMvc;

    @Autowired
    public UserControllerTest(UserRepository userRepository, MockMvc mockMvc) {
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /login")
    void loginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("users/login"));
    }

    @Test
    @DisplayName("GET /register")
    void registerForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("users/new"));
    }

    @Test
    @DisplayName("POST /register")
    void register() throws Exception {
        String email = "example@example.com";
        String password = "qwer1234";
        String name = "example";

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("password", password)
                        .param("name", name)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));

        User user = userRepository.findUserByEmail(email);
        Assertions.assertNotNull(user);
    }
}