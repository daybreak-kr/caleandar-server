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
public class ScheduleServiceTest {

    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;

    private UserBuilder userBuilder = new UserBuilder();
    private ScheduleBuilder scheduleBuilder = new ScheduleBuilder();
    private TeamBuilder teamBuilder = new TeamBuilder();
    private TeamUserBuilder teamUserBuilder = new TeamUserBuilder();

    private Schedule schedule;
    private User user;
    private User newUser;
    private Team team;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public ScheduleServiceTest(ScheduleService scheduleService, ScheduleRepository scheduleRepository, UserRepository userRepository, TeamUserRepository teamUserRepository, TeamRepository teamRepository) {
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.teamUserRepository = teamUserRepository;
        this.teamRepository = teamRepository;
    }


    @BeforeEach
    void setUp() {
        //Entity
        user = userRepository.save(userBuilder.build());
        schedule = scheduleRepository.save(scheduleBuilder.withUser(user).build());
        newUser = userRepository.save(userBuilder.withId(2L).withEmail("example22@example.com").build());
        team = teamRepository.save(teamBuilder.build());

        //create teamUsers
        List<TeamUser> teamUsers = new ArrayList<>();
        teamUsers.add(teamUserBuilder.withTeamAndUser(team, user).build());
        teamUsers.add(teamUserBuilder.withTeamAndUser(team, newUser).build());
        teamUserRepository.saveAll(teamUsers);
    }

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("create schedule")
    public void addSchedule() {

        ScheduleDto.Request request = ScheduleDto.Request.builder()
                .start("2020-10-11T13:00")
                .end("2020-10-11T16:00")
                .title("TEST")
                .description("This is Test").build();

        ScheduleDto.Response oneSchedule = scheduleService.create(user, request);

        Assertions.assertNotNull(oneSchedule.getId());
        Assertions.assertEquals(request.getTitle(), oneSchedule.getTitle());
        Assertions.assertNotNull(oneSchedule.getUser());
        Assertions.assertEquals(oneSchedule.getUser().getId(), user.getId());
    }

    @Test
    @Transactional
    @DisplayName("delete schedule")
    public void delete() {
        scheduleService.delete(user.getEmail(), schedule.getId());
        Assertions.assertFalse(scheduleRepository.existsById(schedule.getId()));
    }

    @Test
    @Transactional
    @DisplayName("update schedule")
    public void update() {
        String newTitle = "NEW TEST";
        String newStartTime = "2022-02-22 13:00";
        String newEndTime = "2022-02-22 14:00";
        String newDescription = "ALL NEW";

        ScheduleDto.Request updateRequest = ScheduleDto.Request.builder()
                .id(schedule.getId())
                .start(newStartTime)
                .end(newEndTime)
                .title(newTitle)
                .description(newDescription).build();

        ScheduleDto.Response updateSchedule = scheduleService.update(user.getEmail(), updateRequest);

        Assertions.assertEquals(updateSchedule.getStart(), newStartTime);
        Assertions.assertEquals(updateSchedule.getTitle(), newTitle);
        Assertions.assertEquals(updateSchedule.getId(), schedule.getId());
    }

    @Test
    @Transactional
    @DisplayName("get all schedules")
    public void getAll() {

        //User 추가
        LocalDateTime start = LocalDateTime.parse("2022-12-12 13:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2022-12-12 22:00", formatter);
        Schedule newSchedule = scheduleRepository.save(scheduleBuilder.withId(2L).withStartAndEnd(start, end).withUser(user).build());


        List<ScheduleDto.Response> list = scheduleService.getAll(user.getEmail());

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(list.get(0).getId(), schedule.getId());
        Assertions.assertEquals(list.get(1).getId(), newSchedule.getId());
    }

    @Test
    @Transactional
    @DisplayName("get one schedule")
    public void getOne() {
        ScheduleDto.Response selectSchedule = scheduleService.getOne(schedule.getId());
        Assertions.assertEquals(selectSchedule.getId(), schedule.getId());
    }

    @Test
    @Transactional
    @DisplayName("create team schedule")
    public void makeAppointment() {

        ScheduleDto.Request teamSchedule = ScheduleDto.Request.builder()
                .start("2022-10-11T13:00")
                .end("2022-10-11T16:00")
                .title("Team Schedule")
                .description("This is Test").build();

        scheduleService.createTeamSchedule(user, teamSchedule, team.getId());

        List<Schedule> child = scheduleRepository.findByUser(user);

        Assertions.assertEquals(user.getSchedules().get(user.getSchedules().size() - 1).getStart(), LocalDateTime.parse(teamSchedule.getStart()));
        Assertions.assertEquals(user.getSchedules().get(user.getSchedules().size() - 1).getStart(), newUser.getSchedules().get(newUser.getSchedules().size() - 1).getStart());


    }

    @Test
    @Transactional
    @DisplayName("create team schedule candidates")
    public void getCandidateSchedules() {

        LocalDateTime start = LocalDateTime.parse("2020-10-11 18:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2020-10-11 20:00", formatter);
        scheduleRepository.save(scheduleBuilder.withStartAndEnd(start, end).withUser(newUser).build());

        LocalDateTime startDate = LocalDateTime.parse("2020-10-11 14:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("2020-10-11 21:00", formatter);

        List<ScheduleDto.Response> teamSchedules = scheduleService.getCandidateSchedules(startDate, endDate, team.getId());

        Assertions.assertEquals(2, teamSchedules.size());
    }
}
