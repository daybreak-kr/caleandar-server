package com.daybreak.cleandar.domain.schedule;

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

    private User example;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setUp() {
        String email = "dev.test@gmail.com";
        String pwd = "1234";
        String name = "Kim";

        example = userRepository.save(User.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .build());

        ScheduleDto.Request request = ScheduleDto.Request.builder()
                .start("2020-10-11 13:00")
                .end("2020-10-11 16:00")
                .title("TEST")
                .description("This is Test").build();

        scheduleRepository.save(request.toEntity(example));


    }

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("일정 비교")
    public void addSchedule() {

        ScheduleDto.Request request = ScheduleDto.Request.builder()
                .start("2020-10-11 13:00")
                .end("2020-11-11 14:00")
                .title("TEST")
                .description("This is Test").build();

        User newUser = userRepository.findUserByEmail("dev.test@gmail.com");
        Schedule schedule = scheduleRepository.save(request.toEntity(newUser));


        Team team = teamRepository.save(Team.builder().name("TEST-TEAM").leader("GilDong").build());
        TeamUser teamUser = TeamUser.builder().team(team).user(example).build();
        TeamUser teamUser2 = TeamUser.builder().team(team).user(newUser).build();

        //TODO Team Create에 포함시켜야하나?
        teamUserRepository.save(teamUser);
        teamUserRepository.save(teamUser2);

        List<User> users = userRepository.findByTeamUserIn(teamUserRepository.findByTeam(team));
        LocalDateTime startDate = LocalDateTime.parse("2020-10-11 15:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("2020-10-11 21:00", formatter);

        List<Schedule> schedules = scheduleRepository.findByUserInAndStartLessThanAndEndGreaterThan(users, endDate, startDate);

        Assertions.assertEquals(2, schedules.size());
    }
}
