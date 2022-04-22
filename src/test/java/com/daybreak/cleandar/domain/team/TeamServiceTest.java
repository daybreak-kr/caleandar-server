package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.builder.TeamBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    TeamBuilder teamBuilder = new TeamBuilder();
    UserBuilder userBuilder = new UserBuilder();

    private Team team;
    private User leader;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(userBuilder.build());
        team = teamBuilder.build(leader);
    }

    @AfterEach
    void tearDown() {
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("list team")
    void index() {
        User user = userRepository.save(userBuilder.withEmail("example2@example.com").withName("example2").build());
        Team team_0 = teamRepository.save(teamBuilder.withName("team1").build(leader));
        Team team_1 = teamRepository.save(teamBuilder.withName("team2").build(leader));
        Team team_2 = teamRepository.save(teamBuilder.withName("team3").build(user));

        List<Team> teams = teamService.index(leader);

        Assertions.assertEquals(teams.size(), 2);
    }

    @Test
    @Transactional
    @DisplayName("create team")
    void create() {
        TeamDto.Request request = TeamDto.Request.builder().name(team.getName()).leader(leader).build();

        Team newTeam = teamService.create(request);

        Assertions.assertNotNull(newTeam);
        Assertions.assertEquals(request.getName(), newTeam.getName());
        Assertions.assertEquals(request.getLeader().getId(), newTeam.getLeader().getId());
    }

    @Test
    @DisplayName("create duplication team")
    void duplicationCreate() {
        TeamDto.Request request = TeamDto.Request.builder().name(team.getName()).leader(leader).build();

        Team newTeam = teamService.create(request);
        Team newTeam2 = teamService.create(request);

        Assertions.assertNotNull(newTeam);
        Assertions.assertEquals(request.getName(), newTeam.getName());
        Assertions.assertEquals(request.getLeader().getId(), newTeam.getLeader().getId());

        Assertions.assertNull(newTeam2);
    }
}