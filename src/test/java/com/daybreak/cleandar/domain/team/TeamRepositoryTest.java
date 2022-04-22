package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.builder.TeamBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    TeamBuilder teamBuilder = new TeamBuilder();
    UserBuilder userBuilder = new UserBuilder();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("find leader's team")
    void findTeamsByLeader() {
        List<User> builders = new ArrayList<>();
        builders.add(userBuilder.build());
        builders.add(userBuilder.withEmail("example2@example.com").withName("example2").build());

        List<User> users = userRepository.saveAll(builders);

        List<Team> teamBuilders = new ArrayList<>();
        teamBuilders.add(teamBuilder.build(users.get(0)));
        teamBuilders.add(teamBuilder.withName("team_2").build(users.get(1)));
        teamBuilders.add(teamBuilder.withName("team_3").build(users.get(1)));

        teamRepository.saveAll(teamBuilders);

        List<Team> results = teamRepository.findTeamsByLeader(users.get(0));

        Assertions.assertEquals(results.size(), 1);
    }
}