package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import com.daybreak.cleandar.security.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
public class ScheduleControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    private String token;

    @Autowired
    public ScheduleControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, ScheduleService scheduleService, UserRepository userRepository, JwtProperties jwtProperties) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }


    @BeforeEach
    void setUser(){
        String email = "dev.test@gmail.com";
        String pwd = "1234";
        String name = "Kim";

        token = jwtProperties.createToken(email);

        userRepository.save(User.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .build());
    }


    @Test
    @Transactional
    @DisplayName("POST /schedules")
    public void addSchedule() throws Exception {

        String title = "TEST";
        String description = "This is Test";

        ScheduleDto.Request request = ScheduleDto.Request.builder()
                .start("2020-10-11 13:00")
                .end("2020-11-11 14:00")
                .title(title)
                .description(description)
                .build();

        String content = objectMapper.writeValueAsString(request);
        User user = userRepository.findUserByEmail("dev.test@gmail.com");

        mockMvc.perform(post("/schedules")
                        .content(content)
                        .param("email", "dev.test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
    }
}