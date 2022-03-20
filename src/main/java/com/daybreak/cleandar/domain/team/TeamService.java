package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

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

    public TeamDto.Response updateTeam(String name,String leader, TeamDto.Request request) {
        if (teamRepository.existsByNameAndLeader(name, leader)) {
            Team team = teamRepository.findByNameAndLeader(name,leader);
            team.updateTeam(request.getName());
            return new TeamDto.Response(teamRepository.save(team));
        }
        return null;
    }

    public boolean deleteTeam(String name,String leader, @PathVariable Long id) {
        if (teamRepository.existsByNameAndLeader(name,leader)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
