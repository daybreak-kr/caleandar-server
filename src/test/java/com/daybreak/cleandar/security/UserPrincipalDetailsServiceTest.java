package com.daybreak.cleandar.security;

import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserDto;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
class UserPrincipalDetailsServiceTest {

    private final UserPrincipalDetailsService userService;
    private final UserRepository userRepository;

    private User user;

    @Autowired
    public UserPrincipalDetailsServiceTest(UserPrincipalDetailsService userService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        user = new UserBuilder().build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 생성")
    void create() {
        UserDto.Request request = UserDto.Request.builder()
                .email(user.getEmail()).password(user.getPassword()).name(user.getName()).build();

        User newUser = userService.create(request);

        Assertions.assertNotNull(newUser);
        Assertions.assertNotNull(newUser.getId());
        Assertions.assertEquals(newUser.getEmail(), user.getEmail());
        Assertions.assertEquals(newUser.getName(), user.getName());
    }

    @Test
    @DisplayName("유저 검색")
    void loadUserByUsername() {
        userRepository.save(user);
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());

        Assertions.assertEquals(user.getEmail(), userDetails.getUsername());
    }
}