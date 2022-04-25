package com.daybreak.cleandar.domain.teamuser;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
    List<TeamUser> findByTeam(Team team);
    List<TeamUser> findTeamUserByUser(User user);
}
