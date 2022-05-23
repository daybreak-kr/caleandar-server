package com.daybreak.cleandar.builder;

import com.daybreak.cleandar.domain.schedule.Schedule;
import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserBuilder {
    public static final String EMAIL = "example@example.com";
    private Long id = 1L;
    private String email = EMAIL;
    private String password = "qwer1234";
    private String name = "example";
    private List<Schedule> schedules = new ArrayList<>();
    private List<TeamUser> teamUsers = new ArrayList<>();

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        return this;
    }

    public UserBuilder withTeamUsers(List<TeamUser> teamUsers){
        this.teamUsers = teamUsers;
        return this;
    }

    public User build() {
        return User.builder().email(email).password(password).name(name).build();
    }
}
