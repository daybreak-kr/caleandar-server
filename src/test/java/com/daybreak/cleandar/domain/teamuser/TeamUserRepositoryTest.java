package com.daybreak.cleandar.domain.teamuser;

import com.daybreak.cleandar.builder.TeamBuilder;
import com.daybreak.cleandar.builder.TeamUserBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamRepository;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamUserRepositoryTest {

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    TeamBuilder teamBuilder = new TeamBuilder();
    UserBuilder userBuilder = new UserBuilder();

    TeamUserBuilder teamUserBuilder = new TeamUserBuilder();

    private User user;

    @BeforeEach
    void setUp() {
        String email = "dev.test@gmail.com";
        String pwd = "1234";
        String name = "GilDong";

        user = userRepository.save(User.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .build());

    }

    @AfterEach
    void tearDown() {
        teamUserRepository.deleteAll();
        userRepository.deleteAll();
        teamRepository.deleteAll();
    }


    @Test
    void findUserByTeamId() {
        String email = "Ahn.test@gmail.com";
        String pwd = "0301";
        String name = "Ahn Thomas";

        User newUser = userRepository.save(User.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .build());

        Team team = teamRepository.save(Team.builder().name("TEST-TEAM").leader(user).build());
        TeamUser teamUser = TeamUser.builder().team(team).user(user).build();
        TeamUser teamUser2 = TeamUser.builder().team(team).user(newUser).build();

        //TODO Team Create에 포함시켜야하나?
        teamUserRepository.save(teamUser);
        teamUserRepository.save(teamUser2);

        List<TeamUser> users = teamUserRepository.findByTeam(team);

        Assertions.assertEquals(2, users.size());
    }

    @Test
    @DisplayName("find user's team")
    void findTeamUserByUser() {
        List<User> builders = new ArrayList<>();
        builders.add(userBuilder.build());
        builders.add(userBuilder.withEmail("example2@example.com").withName("example2").build());

        List<User> users = userRepository.saveAll(builders);

        List<Team> teamBuilders = new ArrayList<>();
        teamBuilders.add(teamBuilder.build(users.get(0)));
        teamBuilders.add(teamBuilder.withName("team_2").build(users.get(1)));
        teamBuilders.add(teamBuilder.withName("team_3").build(users.get(1)));

        teamRepository.saveAll(teamBuilders);

        List<TeamUser> teamUserBuilders = new ArrayList<>();
        teamUserBuilders.add(teamUserBuilder.withTeamAndUser(teamBuilders.get(0), teamBuilders.get(0).getLeader()).build());
        teamUserBuilders.add(teamUserBuilder.withTeamAndUser(teamBuilders.get(1), teamBuilders.get(1).getLeader()).build());
        teamUserBuilders.add(teamUserBuilder.withTeamAndUser(teamBuilders.get(2), teamBuilders.get(2).getLeader()).build());

        teamUserRepository.saveAll(teamUserBuilders);

        List<TeamUser> results = teamUserRepository.findTeamUserByUser(users.get(0));
        List<TeamUser> results_2 = teamUserRepository.findTeamUserByUser(users.get(1));

        Assertions.assertEquals(results.size(), 1);
        Assertions.assertEquals(results_2.size(), 2);
    }
}
