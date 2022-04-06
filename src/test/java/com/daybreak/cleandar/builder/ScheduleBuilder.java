package com.daybreak.cleandar.builder;

import com.daybreak.cleandar.domain.schedule.Schedule;
import com.daybreak.cleandar.domain.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleBuilder {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Long id = 1L;
    private LocalDateTime start = LocalDateTime.parse("2020-10-11 13:00", formatter);
    private LocalDateTime end = LocalDateTime.parse("2020-10-11 16:00", formatter);
    private String title = "TEST";
    private String description = "This is Test";
    private User user;

    public ScheduleBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ScheduleBuilder withStartAndEnd(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
        return this;
    }

    public ScheduleBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Schedule build() {
        return Schedule.builder()
                .start(start)
                .end(end)
                .title(title)
                .description(description)
                .user(user)
                .build();
    }

}
