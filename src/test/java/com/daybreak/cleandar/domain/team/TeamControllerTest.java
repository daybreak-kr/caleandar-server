package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.builder.TeamBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.teamuser.TeamUserRepository;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class TeamControllerTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private MockMvc mockMvc;

    private UserBuilder userBuilder = new UserBuilder();
    private TeamBuilder teamBuilder = new TeamBuilder();

    Team team = null;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(userBuilder.build());
        team = teamRepository.save(teamBuilder.withName("team2").build(user));
    }

    @AfterEach
    void tearDown() {
        teamUserRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = UserBuilder.EMAIL)
    @DisplayName("GET /teams")
    void index() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teams"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("teams/index"));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = UserBuilder.EMAIL)
    @DisplayName("GET /teams/new")
    void teamForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teams/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("teams/new"));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = UserBuilder.EMAIL)
    @DisplayName("GET /teams/:id")
    void show() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teams/" + team.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("teams/show"));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = UserBuilder.EMAIL)
    @DisplayName("GET /teams/:id/edit")
    void edit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teams/" + team.getId() + "/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("teams/edit"));
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = UserBuilder.EMAIL)
    @DisplayName("POST /teams")
    void create() throws Exception {
        Team team = teamBuilder.withName("test-team").build(userBuilder.build());

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .param("name", team.getName())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = UserBuilder.EMAIL)
    @DisplayName("PUT /teams/:id")
    void update() throws Exception {
        String newName = "new-team";

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/" + team.getId())
                        .param("name", newName)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}