package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findTeamsByLeader(User leader);
}
