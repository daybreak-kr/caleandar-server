package com.daybreak.cleandar.builder;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class TeamBuilder {
    private Long id = 1L;
    private String name = "test-team";
    private String leader = "example";
    private List<TeamUser> teamUsers = new ArrayList<>();
    private User user;

    public TeamBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TeamBuilder withTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
        return this;
    }

    public Team build() {
        return Team.builder().leader(leader).name(name).build();
    }
}
