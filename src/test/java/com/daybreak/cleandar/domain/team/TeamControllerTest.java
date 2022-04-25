package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.builder.TeamBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
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
    private MockMvc mockMvc;

    private UserBuilder userBuilder = new UserBuilder();
    private TeamBuilder teamBuilder = new TeamBuilder();

    @BeforeEach
    void setUp() {
        userRepository.save(userBuilder.build());
    }

    @AfterEach
    void tearDown() {
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
    @DisplayName("POST /teams")
    void create() throws Exception {
        Team team = teamBuilder.build(userBuilder.build());

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .param("name", team.getName())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}