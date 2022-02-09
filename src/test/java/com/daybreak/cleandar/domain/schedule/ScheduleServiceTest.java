package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ScheduleServiceTest {

    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ScheduleServiceTest(ScheduleService scheduleService, ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }


    @BeforeEach
    void setUp(){
        String email = "dev.test@gmail.com";
        String pwd = "1234";
        String name = "Kim";

        userRepository.save(User.builder()
                .email(email)
                .password(pwd)
                .name(name)
                .build());
    }

    @AfterEach
    void tearDown(){
        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("일정 생성")
    public void addSchedule() {

        ScheduleDto.Request request = ScheduleDto.Request.builder()
                .start("2020-10-11 13:00")
                .end("2020-11-11 14:00")
                .title("TEST")
                .description("This is Test").build();

        User user = userRepository.findUserByEmail("dev.test@gmail.com");
        Schedule schedule = scheduleService.addSchedule("dev.test@gmail.com", request);


        Assertions.assertNotNull(schedule.getId());
        Assertions.assertEquals(request.getTitle(), schedule.getTitle());
        Assertions.assertNotNull(schedule.getUser());
        Assertions.assertEquals(schedule.getUser().getId(), user.getId());
    }



}
