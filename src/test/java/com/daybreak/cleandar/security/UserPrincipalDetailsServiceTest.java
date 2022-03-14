package com.daybreak.cleandar.security;

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

    @Autowired
    public UserPrincipalDetailsServiceTest(UserPrincipalDetailsService userService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 생성")
    void create() {
        String email = "example@example.com";
        String password = "qwer1234";
        String name = "example";

        UserDto.Request request = UserDto.Request.builder()
                .email(email).password(password).name(name).build();

        User user = userService.create(request);

        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(user.getEmail(), email);
        Assertions.assertEquals(user.getName(), name);
    }

    @Test
    @DisplayName("유저 검색")
    void loadUserByUsername() {
        String email = "example@example.com";
        String password = "qwer1234";
        String name = "example";

        userRepository.save(User.builder().email(email).password(password).name(name).build());

        UserDetails userDetails = userService.loadUserByUsername(email);

        Assertions.assertEquals(email, userDetails.getUsername());
    }
}