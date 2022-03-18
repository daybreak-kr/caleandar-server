package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamDto;
import com.daybreak.cleandar.domain.team.TeamService;
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
public class ScheduleServiceTest {

    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final TeamService teamService;
    private final TeamUserRepository teamUserRepository;

    private Schedule schedule;
    private ScheduleDto.Request request;
    private User user;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public ScheduleServiceTest(ScheduleService scheduleService, ScheduleRepository scheduleRepository, UserRepository userRepository, TeamService teamService, TeamUserRepository teamUserRepository) {
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.teamService = teamService;
        this.teamUserRepository = teamUserRepository;
    }


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

        request = ScheduleDto.Request.builder()
                .start("2020-10-11 13:00")
                .end("2020-10-11 16:00")
                .title("TEST")
                .description("This is Test").build();

        schedule = scheduleService.create(user, request);


    }

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("일정 생성")
    public void addSchedule() {

        Assertions.assertNotNull(schedule.getId());
        Assertions.assertEquals(request.getTitle(), schedule.getTitle());
        Assertions.assertNotNull(schedule.getUser());
        Assertions.assertEquals(schedule.getUser().getId(), user.getId());
    }

    @Test
    @Transactional
    @DisplayName("일정 삭제")
    public void delete() {

        scheduleService.delete("dev.test@gmail.com", schedule.getId());

        Assertions.assertFalse(scheduleRepository.existsById(schedule.getId()));
    }

    @Test
    @Transactional
    @DisplayName("일정 수정")
    public void update() {

        //수정
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

        Schedule updateSchedule = scheduleService.update("dev.test@gmail.com", updateRequest);

        Assertions.assertEquals(updateSchedule.getStart(), LocalDateTime.parse(newStartTime, formatter));
        Assertions.assertNotEquals(updateSchedule.getStart(), LocalDateTime.parse("2020-10-11 13:00", formatter));
        Assertions.assertEquals(updateSchedule.getTitle(), newTitle);
        Assertions.assertNotEquals("TEST", updateSchedule.getTitle());
    }

    @Test
    @Transactional
    @DisplayName("일정 전체 조회")
    public void getAll() {

        ScheduleDto.Request request2 = ScheduleDto.Request.builder()
                .start("2022-12-12 13:00")
                .end("2022-12-12 22:00")
                .title("newTitle")
                .description("newDescription").build();

        Schedule schedule2 = scheduleService.create(user, request2);

        List<Schedule> list = scheduleService.getAll("dev.test@gmail.com");

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(list.get(0).getId(), schedule.getId());
        Assertions.assertEquals(list.get(1).getId(), schedule2.getId());
    }

    @Test
    @Transactional
    @DisplayName("일정 조회")
    public void getOne() {
        Schedule selectSchedule = scheduleService.getOne(schedule.getId());

        Assertions.assertEquals(selectSchedule, schedule);
    }

    @Test
    @Transactional
    @DisplayName("팀 일정 생성")
    public void makeAppointment() {
        String email = "Ahn.test@gmail.com";
        String pwd = "0301";
        String name = "Ahn Thomas";

        User newUser = userRepository.save(User.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .build());

        ScheduleDto.Request teamShedule = ScheduleDto.Request.builder()
                .start("2022-10-11 13:00")
                .end("2022-10-11 16:00")
                .title("Team Schedule")
                .description("This is Test").build();


        Team team = teamService.createTeam(TeamDto.Request.builder().name("TEST-TEAM").leader("GilDong").build());

        TeamUser.builder().team(team).user(user).build();
        TeamUser.builder().team(team).user(newUser).build();

        scheduleService.createTeamSchedule(user, teamShedule, team.getId());

        Assertions.assertEquals(user.getSchedules().get(user.getSchedules().size() - 1).getStart(), LocalDateTime.parse(teamShedule.getStart(), formatter));
        Assertions.assertEquals(user.getSchedules().get(user.getSchedules().size() - 1).getStart(), newUser.getSchedules().get(newUser.getSchedules().size() - 1).getStart());


    }

    @Test
    @Transactional
    @DisplayName("팀 일정 후보 생성")
    public void getCandidateSchedules() {

        String email = "Ahn.test@gmail.com";
        String pwd = "0301";
        String name = "Ahn Thomas";

        User newUser = userRepository.save(User.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .build());


        Team team = teamService.createTeam(TeamDto.Request.builder().name("TEST-TEAM").leader("GilDong").build());

        TeamUser teamUser = TeamUser.builder().team(team).user(user).build();
        TeamUser teamUser2 = TeamUser.builder().team(team).user(newUser).build();

        teamUserRepository.save(teamUser);
        teamUserRepository.save(teamUser2);

        scheduleService.create(newUser, ScheduleDto.Request.builder()
                .start("2020-10-11 18:00")
                .end("2020-10-11 20:00")
                .title("Ahn_TEST")
                .description("Ahn_Schedule").build());

        LocalDateTime startDate = LocalDateTime.parse("2020-10-11 14:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("2020-10-11 21:00", formatter);

        List<ScheduleDto.Response> teamSchedules = scheduleService.getCandidateSchedules(startDate, endDate, team.getId());

        for (ScheduleDto.Response ts : teamSchedules) {
            System.out.println("!!!!!!!!!" + ts.getStart() + " ~ " + ts.getEnd());
        }

        Assertions.assertEquals(2, teamSchedules.size());
    }

}
