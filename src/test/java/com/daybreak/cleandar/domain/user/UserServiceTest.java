package com.daybreak.cleandar.domain.user;

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

    @Test
    @Transactional
    @DisplayName("유저 생성")
    void create() {
        String email = "example@example.com";
        String password = "qwer1234";
        String name = "example";

        UserDto.Request request = new UserDto.Request();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);

        User user = userService.create(request);

        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
    }
}