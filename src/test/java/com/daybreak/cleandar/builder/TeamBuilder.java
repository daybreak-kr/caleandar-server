package com.daybreak.cleandar.builder;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.user.User;

public class TeamBuilder {
    private Long id = 1L;
    private String name = "test-team";

    public TeamBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TeamBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Team build(User leader) {
        return Team.builder().name(name).leader(leader).build();
    }
}
