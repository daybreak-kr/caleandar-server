package com.daybreak.cleandar.domain.user;

import com.daybreak.cleandar.security.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        String email = "example@example.com";
        token = jwtProperties.createToken(email);
        user = userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode("qwer1234"))
                .name("example")
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("POST /api/users")
    void createUser() throws Exception {
        String email = "example2@example.com";
        String password = "qwer1234";
        String name = "example2";

        UserDto.Request request = UserDto.Request.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name));
    }

    @Test
    @Transactional
    @DisplayName("PUT /api/users")
    void updateUser() throws Exception {
        String name = "example3";

        UserDto.Request request = UserDto.Request.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(name)
                .build();

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users")
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + token)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name));
    }

    @Test
    @Transactional
    @DisplayName("POST /login")
    void login() throws Exception {
        String email = "example2@example.com";
        String password = "qwer1234";
        String name = "example2";
        String token = jwtProperties.TOKEN_PREFIX + jwtProperties.createToken(email);

        userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .name(name)
                .build());

        UserDto.Request request = UserDto.Request.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().stringValues(jwtProperties.HEADER_STRING, token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name));
    }

}