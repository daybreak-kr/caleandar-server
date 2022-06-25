package com.daybreak.cleandar.builder;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.user.User;

public class TeamUserBuilder {
    private Long id = 1L;
    private Team team;
    private User user;
    private String status = "wait";

    public TeamUserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TeamUserBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public TeamUserBuilder withTeamAndUser(Team team, User user) {
        this.team = team;
        this.user = user;
        return this;
    }

    public TeamUser build() {
        return TeamUser.builder().user(user).team(team).build();
    }

}
