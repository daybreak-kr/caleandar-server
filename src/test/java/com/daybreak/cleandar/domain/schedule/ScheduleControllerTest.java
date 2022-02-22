package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import com.daybreak.cleandar.security.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
public class ScheduleControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;
    private final ScheduleService scheduleService;

    private String token;
    private ScheduleDto.Request request;

    @Autowired
    public ScheduleControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserRepository userRepository, JwtProperties jwtProperties, ScheduleService scheduleService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
        this.scheduleService = scheduleService;
    }


    @BeforeEach
    void setUser(){
        String email = "dev.test@gmail.com";
        String pwd = "1234";
        String name = "Kim";

        token = jwtProperties.createToken(email);

        userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode(pwd))
                .name(name)
                .build());

        String title = "TEST";
        String description = "This is Test";
        String start = "2020-10-11 13:00";

        request = ScheduleDto.Request.builder()
                .start(start)
                .end("2020-11-11 14:00")
                .title(title)
                .description(description)
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    @Transactional
    @DisplayName("POST /schedules")
    public void create() throws Exception {

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/schedules")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.start").value("2020-10-11 13:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("TEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("This is Test"));
    }

    @Test
    @Transactional
    @DisplayName("DELETE /schedules")
    public void delete() throws Exception {

        Schedule schedule = scheduleService.create("dev.test@gmail.com", request);

        String content = objectMapper.writeValueAsString(schedule.getId());
        System.out.println(schedule.getId());

        // 다른 유저가 삭제
        userRepository.save(User.builder()
                .email("fake.test@gmail.com")
                .password(new BCryptPasswordEncoder().encode("1111"))
                .name("test")
                .build());

        String fakeToken = jwtProperties.createToken("fake.test@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/schedules/{id}", schedule.getId())
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + fakeToken))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("false"));

        // 일정을 작성한 유저가 삭제
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/schedules/{id}", schedule.getId())
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    @Transactional
    @DisplayName("PUT /schedules")
    public void update() throws Exception {

        // 일정 저장
        Schedule schedule = scheduleService.create("dev.test@gmail.com", request);

        // 수정할 내용
        String newTitle = "NEW TEST";
        String newStartTime = "2022-02-22 13:00";
        String newEndTime = "2022-02-22 14:00";
        String newDescription = "ALL NEW";

        ScheduleDto.Request updateRequest = ScheduleDto.Request.builder()
                .id(schedule.getId())
                .title(newTitle)
                .start(newStartTime)
                .end(newEndTime)
                .description(newDescription)
                .build();

        String content = objectMapper.writeValueAsString(updateRequest);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        mockMvc.perform(put("/api/schedules/{id}", schedule.getId())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.start").value(newStartTime))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(newTitle))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(newDescription));
    }

    @Test
    @Transactional
    @DisplayName("GET /schedules")
    public void getAll() throws Exception {
        ScheduleDto.Request request1 = ScheduleDto.Request.builder()
                .start("2020-10-11 13:00")
                .end("2020-11-11 14:00")
                .title("TEST")
                .description("This is Test").build();

        Schedule schedule1 = scheduleService.create("dev.test@gmail.com", request1);

        ScheduleDto.Request request2 = ScheduleDto.Request.builder()
                .start("2022-12-12 13:00")
                .end("2022-12-12 22:00")
                .title("newTitle")
                .description("newDescription").build();

        Schedule schedule2 = scheduleService.create("dev.test@gmail.com", request2);

        mockMvc.perform(get("/api/schedules")
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists());
    }
}