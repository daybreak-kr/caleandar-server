package com.daybreak.cleandar.domain.user;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamRepository;
import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.teamuser.TeamUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    private User example;

    @BeforeEach
    void setUp() {
        example = userRepository.save(User.builder()
                .email("example@example.com")
                .password(new BCryptPasswordEncoder().encode("qwer1234"))
                .name("example")
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 이메일 검색")
    void findUserByEmail() {
        String email = "example@example.com";
        User user = userRepository.findUserByEmail(email);

        assertNotNull(user);
        assertEquals(user.getEmail(), email);
    }

    @Test
    @DisplayName("팀 유저 검색")
    void findByTeamUser() {
        String email = "Ahn.test@gmail.com";
        String pwd = "0301";
        String name = "Ahn Thomas";

        User newUser = userRepository.save(User.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .build());

        Team team = teamRepository.save(Team.builder().name("TEST-TEAM").leader("GilDong").build());
        TeamUser teamUser = TeamUser.builder().team(team).user(example).build();
        TeamUser teamUser2 = TeamUser.builder().team(team).user(newUser).build();

        //TODO Team Create에 포함시켜야하나?
        teamUserRepository.save(teamUser);
        teamUserRepository.save(teamUser2);

        List<User> users = userRepository.findByTeamUserIn(teamUserRepository.findByTeam(team));

        Assertions.assertEquals(2, users.size());
    }
}