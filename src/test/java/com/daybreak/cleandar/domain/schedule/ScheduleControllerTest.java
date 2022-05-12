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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
public class ScheduleControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;

    private UserBuilder userBuilder = new UserBuilder();
    private ScheduleBuilder scheduleBuilder = new ScheduleBuilder();
    private TeamBuilder teamBuilder = new TeamBuilder();
    private TeamUserBuilder teamUserBuilder = new TeamUserBuilder();

    private Schedule schedule;
    private User user;
    private Team team;
    private User newUser;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public ScheduleControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserRepository userRepository, ScheduleService scheduleService, ScheduleRepository scheduleRepository, TeamRepository teamRepository, TeamUserRepository teamUserRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
        this.teamRepository = teamRepository;
        this.teamUserRepository = teamUserRepository;
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
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("GET /schedules/new")
    public void scheduleCreateForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("schedules/new"));
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("GET /schedules/{id}/edit")
    public void scheduleEditForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/{id}/edit", schedule.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("schedules/edit"));
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("GET /schedules")
    public void getAll() throws Exception {

        scheduleRepository.save(scheduleBuilder.withId(2L).withUser(user).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.view().name("schedules/list"));
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("GET /schedules/{id}")
    public void getOne() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/{id}", schedule.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.view().name("schedules/show"));
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("GET /schedules/candidates")
    public void getCandidates() throws Exception {

        LocalDateTime start = LocalDateTime.parse("2020-10-11 18:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2020-10-11 20:00", formatter);
        scheduleRepository.save(scheduleBuilder.withStartAndEnd(start, end).withUser(newUser).build());

        String startDate = "2020-10-11T14:00";
        String endDate = "2020-10-11T21:00";

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/candidates")
                        .param("start", startDate)
                        .param("end", endDate)
                        .param("teamId", team.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("teams/candidates"));
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("POST /schedules")
    void create() throws Exception {

        String title = "abc";
        String description = "This is Test";
        String start = "2020-10-11T13:00";
        String end = "2020-10-12T13:00";

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/new")
                        .param("title", title)
                        .param("description", description)
                        .param("start", start)
                        .param("end", end)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("POST /schedules/team/{id}")
    public void createTeamSchedule() throws Exception {
        String title = "abc";
        String description = "This is Test";
        String start = "2020-10-11T13:00";
        String end = "2020-10-12T13:00";

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/team/{id}", team.getId())
                        .param("title", title)
                        .param("description", description)
                        .param("start", start)
                        .param("end", end)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.view().name("teams/show"));
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("DELETE /schedules")
    public void delete() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/schedules/{id}", schedule.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andDo(print());
    }

    @Test
    @Transactional
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "example@example.com")
    @DisplayName("PUT /schedules")
    public void update() throws Exception {

        // 수정할 내용
        String newTitle = "NEW TEST";
        String newStartTime = "2022-02-22T13:00";
        String newEndTime = "2022-02-22T14:00";
        String newDescription = "ALL NEW";

        mockMvc.perform(MockMvcRequestBuilders.put("/schedules/{id}", schedule.getId())
                        .param("title", newTitle)
                        .param("description", newDescription)
                        .param("start", newStartTime)
                        .param("end", newEndTime)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.view().name("schedules/show"));
    }
}