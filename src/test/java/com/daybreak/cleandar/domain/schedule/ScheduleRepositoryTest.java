package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.builder.ScheduleBuilder;
import com.daybreak.cleandar.builder.TeamBuilder;
import com.daybreak.cleandar.builder.TeamUserBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamRepository;
import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.teamuser.TeamUserRepository;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    private UserBuilder userBuilder = new UserBuilder();
    private ScheduleBuilder scheduleBuilder = new ScheduleBuilder();
    private TeamBuilder teamBuilder = new TeamBuilder();
    private TeamUserBuilder teamUserBuilder = new TeamUserBuilder();
    private User user;
    private Schedule schedule;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setUp() {
        user = userRepository.save(userBuilder.build());
        schedule = scheduleRepository.save(scheduleBuilder.withUser(user).build());
    }

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("특정 기간에 속하는 팀 멤버들의 일정 찾기")
    public void findNotPossibleSchedules() {

        LocalDateTime start = LocalDateTime.parse("2020-10-11 18:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2020-10-11 20:00", formatter);

        User newUser = userRepository.save(userBuilder.withId(2L).withEmail("example22@example.com").build());
        scheduleRepository.save(scheduleBuilder.withStartAndEnd(start, end).withUser(newUser).build());

        Team team = teamRepository.save(teamBuilder.build(user));

        //create teamUsers
        List<TeamUser> teamUsers = new ArrayList<>();
        teamUsers.add(teamUserBuilder.withTeamAndUser(team, user).build());
        teamUsers.add(teamUserBuilder.withTeamAndUser(team, newUser).build());
        teamUserRepository.saveAll(teamUsers);

        List<User> users = userRepository.findByTeamUserIn(teamUserRepository.findByTeam(team));
        LocalDateTime startDate = LocalDateTime.parse("2020-10-11 14:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("2020-10-11 21:00", formatter);

        List<Schedule> schedules = scheduleRepository.findByUserInAndEndGreaterThanAndStartLessThan(users, startDate, endDate);

        Assertions.assertEquals(2, schedules.size());
    }
}
