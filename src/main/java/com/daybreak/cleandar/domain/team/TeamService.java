package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.teamuser.TeamUserRepository;
import com.daybreak.cleandar.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;

    public List<Team> index(User user) {
        return teamUserRepository.findTeamUserByUser(user).stream().map(TeamUser::getTeam).collect(Collectors.toList());
    }

    public Team create(@RequestBody TeamDto.Request request) {
        try {
            Team team = teamRepository.save(Team.builder().name(request.getName()).leader(request.getLeader()).build());
            teamUserRepository.save(TeamUser.builder().team(team).user(request.getLeader()).build());
            return team;
        } catch (DataIntegrityViolationException exception) {
            return null;
        }
    }
}
