package com.daybreak.cleandar.domain.teamuser;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamRepository;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


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

        Team team = teamRepository.save(Team.builder().name("TEST-TEAM").leader("GilDong").build());
        TeamUser teamUser = TeamUser.builder().team(team).user(user).build();
        TeamUser teamUser2 = TeamUser.builder().team(team).user(newUser).build();

        //TODO Team Create에 포함시켜야하나?
        teamUserRepository.save(teamUser);
        teamUserRepository.save(teamUser2);

        List<TeamUser> users = teamUserRepository.findByTeam(team);

        Assertions.assertEquals(2, users.size());
    }
}
