package com.daybreak.cleandar.domain.user;

import com.daybreak.cleandar.builder.TeamBuilder;
import com.daybreak.cleandar.builder.TeamUserBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamRepository;
import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.teamuser.TeamUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
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

    private UserBuilder userBuilder = new UserBuilder();
    private TeamBuilder teamBuilder = new TeamBuilder();
    private TeamUserBuilder teamUserBuilder = new TeamUserBuilder();

    private User example;

    @BeforeEach
    void setUp() {
        example = userRepository.save(userBuilder.build());
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

        User newUser = userRepository.save(userBuilder.withId(2L).withEmail("example22@example.com").build());
        Team team = teamRepository.save(teamBuilder.build(example));

        List<TeamUser> teamUsers = new ArrayList<>();
        teamUsers.add(teamUserBuilder.withTeamAndUser(team, example).build());
        teamUsers.add(teamUserBuilder.withTeamAndUser(team, newUser).build());
        teamUserRepository.saveAll(teamUsers);

        List<User> users = userRepository.findByTeamUserIn(teamUserRepository.findByTeam(team));

        Assertions.assertEquals(2, users.size());
    }

    @Test
    @DisplayName("유저 이름 및 이메일 검색")
    void findAllByNameOrEmail() {
        User newUser = userRepository.save(userBuilder.withId(2L).withName("ebs").withEmail("example22@example.com").build());
        User newUser2 = userRepository.save(userBuilder.withId(2L).withName("kim").withEmail("example33@example.com").build());

        List<User> users = userRepository.findAllByNameStartingWithOrEmailStartingWith("e", "e");

        Assertions.assertEquals(3, users.size());
    }
}