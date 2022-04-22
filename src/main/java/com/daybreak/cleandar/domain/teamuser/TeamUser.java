package com.daybreak.cleandar.domain.teamuser;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teamsusers")
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teams_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @Builder
    public TeamUser(Team team, User user) {
        this.team = team;
        this.user = user;
        this.team.getTeamUser().add(this);
    }
}
