package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public List<Team> index(User leader) {
        return teamRepository.findTeamsByLeader(leader);
    }

    public Team create(@RequestBody TeamDto.Request request) {
        try {
            return teamRepository.save(Team.builder().name(request.getName()).leader(request.getLeader()).build());
        } catch (DataIntegrityViolationException exception) {
            return null;
        }
    }
}
