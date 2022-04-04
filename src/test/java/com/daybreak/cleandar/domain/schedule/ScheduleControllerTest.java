package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.builder.ScheduleBuilder;
import com.daybreak.cleandar.builder.UserBuilder;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
public class ScheduleControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;

    private UserBuilder userBuilder = new UserBuilder();
    private ScheduleBuilder scheduleBuilder = new ScheduleBuilder();

    private ScheduleDto.Request request;
    private Schedule schedule;
    private User user;

    @Autowired
    public ScheduleControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserRepository userRepository, ScheduleService scheduleService, ScheduleRepository scheduleRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
    }


    @BeforeEach
    void setUp() {
//        String email = "dev.test@gmail.com";
//        String pwd = "1234";
//        String name = "Kim";
//
//        user = userRepository.save(User.builder()
//                .email(email)
//                .password(pwd)
//                .name(name)
//                .build());
////
//        String title = "TEST";
//        String description = "This is Test";
//        String start = "2020-10-11 13:00";
//        String end = "2020-10-12 13:00";
//////
//        request = ScheduleDto.Request.builder()
//                .start(start)
//                .end(end)
//                .title(title)
//                .description(description)
//                .build();
        user = userRepository.save(userBuilder.build());
        schedule = scheduleRepository.save(scheduleBuilder.withUser(user).build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    @WithMockUser
    @DisplayName("POST /schedules")
    void create() throws Exception {

        String title = "abc";
        String description = "This is Test";
        String start = "2020-10-11 13:00";
        String end = "2020-10-12 13:00";

        request = ScheduleDto.Request.builder()
                .start(start)
                .end(end)
                .title(title)
                .description(description)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //TODO NullPointerException
        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
//                        .param("title", title)
//                        .param("description", description)
//                        .param("start", start)
//                        .param("end", end))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.view().name("schedules/detail"));
    }

//    @Test
//    @WithMockUser
//    @DisplayName("DELETE /schedules")
//    public void delete() throws Exception {
//        String url = "/schedules/" + schedule.getId();
//        // 일정을 작성한 유저가 삭제
//        mockMvc.perform(MockMvcRequestBuilders.delete(url, schedule.getId())
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.content().string("true"));
//    }
//
//    @Test
//    @WithMockUser
//    @Transactional
//    @DisplayName("PUT /schedules")
//    public void update() throws Exception {
//
//        // 수정할 내용
//        String newTitle = "NEW TEST";
//        String newStartTime = "2022-02-22 13:00";
//        String newEndTime = "2022-02-22 14:00";
//        String newDescription = "ALL NEW";
//
//        ScheduleDto.Request updateRequest = ScheduleDto.Request.builder()
//                .id(schedule.getId())
//                .title(newTitle)
//                .start(newStartTime)
//                .end(newEndTime)
//                .description(newDescription)
//                .build();
//
//        String content = objectMapper.writeValueAsString(updateRequest);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/schedules/{id}", schedule.getId())
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.start").value(newStartTime))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(newTitle))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(newDescription))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.updateAt").exists());
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("GET /schedules")
//    public void getAll() throws Exception {
//
//        ScheduleDto.Request request2 = ScheduleDto.Request.builder()
//                .start("2022-12-12 13:00")
//                .end("2022-12-12 22:00")
//                .title("newTitle")
//                .description("newDescription").build();
//
//        scheduleService.create(user, request2);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/schedules"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists());
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("GET /schedules/{id}")
//    public void getOne() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/{id}", schedule.getId()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.start").value(schedule.getStart().toString().replace('T', ' ')))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(schedule.getTitle()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(schedule.getDescription()));
//    }
}