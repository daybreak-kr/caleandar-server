package com.daybreak.cleandar.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        String email = "example@example.com";
        String password = "qwer1234";
        String name = "example";

        userRepository.save(User.builder()
                .email(email)
                .password(password)
                .name(name)
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("유저 생성")
    void create() {
        String email = "example2@example.com";
        String password = "qwer1234";
        String name = "example2";

        UserDto.Request request = UserDto.Request.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();

        User user = userService.create(request);

        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
    }

    @Test
    @Transactional
    @DisplayName("유저 변경")
    void update() {
        String email = "example@example.com";
        String name = "example3";
        User user = userRepository.findUserByEmail(email);

        UserDto.Request request = UserDto.Request.builder()
                .email(email)
                .name(name)
                .build();

        UserDto.Response updateUser = userService.update(user, request);

        assertNotNull(user.getId());
        assertEquals(email, updateUser.getEmail());
        assertEquals(name, updateUser.getName());
    }
}