package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.builder.TeamBuilder;
import com.daybreak.cleandar.builder.TeamUserBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.teamuser.TeamUserRepository;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserDto;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamUserRepository teamUserRepository;

    TeamBuilder teamBuilder = new TeamBuilder();
    UserBuilder userBuilder = new UserBuilder();
    TeamUserBuilder teamUserBuilder = new TeamUserBuilder();

    private Team team;
    private User leader;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(userBuilder.build());
        team = teamBuilder.build(leader);
    }

    @AfterEach
    void tearDown() {
        teamUserRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("list team")
    void index() {
        User user = userRepository.save(userBuilder.withEmail("example2@example.com").withName("example2").build());

        List<Team> teamBuilders = new ArrayList<>();
        teamBuilders.add(teamBuilder.withName("team1").build(leader));
        teamBuilders.add(teamBuilder.withName("team2").build(leader));
        teamBuilders.add(teamBuilder.withName("team3").build(user));

        List<Team> teams = teamRepository.saveAll(teamBuilders);
        List<TeamUser> teamUserBuilders = new ArrayList<>();
        for (Team team : teams) {
            teamUserBuilders.add(teamUserBuilder.withTeamAndUser(team, team.getLeader()).build());
        }
        teamUserRepository.saveAll(teamUserBuilders);

        List<Team> results = teamService.index(leader);

        Assertions.assertEquals(results.size(), 2);
    }

    @Test
    @DisplayName("show team")
    void show() {
        Team team = teamRepository.save(teamBuilder.withName("team1").build(leader));
        TeamUser teamUser = teamUserRepository.save(teamUserBuilder.withTeamAndUser(team, team.getLeader()).build());

        Team result = teamService.show(team.getId());

        Assertions.assertEquals(result.getId(), team.getId());
        Assertions.assertEquals(result.getId(), teamUser.getTeam().getId());
    }

    @Test
    @Transactional
    @DisplayName("create team")
    void create() {
        TeamDto.Request request = TeamDto.Request.builder().name(team.getName()).leader(leader).build();

        Team newTeam = teamService.create(request);

        TeamUser teamUser = teamUserRepository.findTeamUserByUser(newTeam.getLeader()).get(0);

        Assertions.assertNotNull(newTeam);
        Assertions.assertEquals(request.getName(), newTeam.getName());
        Assertions.assertEquals(request.getLeader().getId(), newTeam.getLeader().getId());

        Assertions.assertEquals(request.getLeader().getId(), teamUser.getUser().getId());
        Assertions.assertEquals(newTeam.getId(), teamUser.getTeam().getId());
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

    @Test
    @Transactional
    @DisplayName("update team")
    void update() {
        TeamDto.Request request = TeamDto.Request.builder().name(team.getName()).leader(leader).build();
        Team team = teamService.create(request);

        String newName = "new Team";
        request.setId(team.getId());
        request.setName(newName);

        Team updateTeam = teamService.update(request);

        Assertions.assertNotNull(updateTeam);
        Assertions.assertEquals(request.getId(), updateTeam.getId());
        Assertions.assertEquals(newName, updateTeam.getName());
        Assertions.assertEquals(request.getLeader().getId(), updateTeam.getLeader().getId());
    }

    @Test
    @Transactional
    @DisplayName("delete team")
    void delete() {
        TeamDto.Request request = TeamDto.Request.builder().name(team.getName()).leader(leader).build();
        Team team = teamService.create(request);

        Team deleteTeam = teamService.delete(leader, team.getId());

        Assertions.assertNotNull(deleteTeam);
        Assertions.assertFalse(teamRepository.findById(team.getId()).isPresent());
        Assertions.assertEquals(0, teamUserRepository.findByTeam(team).size());
    }

    @Test
    @Transactional
    @DisplayName(("invite members"))
    void invite() {
        TeamDto.Request request = TeamDto.Request.builder().name(team.getName()).leader(leader).build();
        Team team = teamService.create(request);

        List<UserDto.Response> users = new ArrayList<>();
        users.add(new UserDto.Response(userRepository.save(userBuilder
                .withId(2L)
                .withName("kim")
                .withEmail("test1@test.com").build())));
        users.add(new UserDto.Response(userRepository.save(userBuilder
                .withId(2L).withName("lee").withEmail("test2@test.com").build())));
        teamService.invite(team.getId(), users);

        Assertions.assertEquals(2, teamUserRepository.findTeamUserByStatusAndTeam("wait", team).size());

    }
}