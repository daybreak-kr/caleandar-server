package com.daybreak.cleandar.builder;

import com.daybreak.cleandar.domain.schedule.Schedule;
import com.daybreak.cleandar.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserBuilder {
    private Long id = 1L;
    private String email = "example@example.com";
    private String password = "qwer1234";
    private String name = "example";
    private List<Schedule> schedules = new ArrayList<>();

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        return this;
    }

    public User build() {
        return User.builder().email(email).password(password).name(name).build();
    }
}
