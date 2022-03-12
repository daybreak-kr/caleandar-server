package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Team createTeam(@RequestBody TeamDto.Request request) {
        return teamRepository.save(Team.builder()
                .name(request.getName())
                .leader(request.getLeader())
                .build());
    }

    public TeamDto.Response updateTeam(Team team, String name) {
        if (teamRepository.existsByNameAndLeader(team.getName(), team.getLeader())) {
            team.updateTeam(name);
            return new TeamDto.Response(teamRepository.save(team));
        }
        return null;
    }

    public boolean deleteTeam(Team team) {
        if (teamRepository.existsByNameAndLeader(team.getName(), team.getLeader())) {
            teamRepository.delete(team);
            return true;
        }
        return false;
    }
}
