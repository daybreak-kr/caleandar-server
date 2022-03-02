package com.daybreak.cleandar.domain.team;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.Optional;

//비지니스 로직
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    //팀 생성
    public Team createTeam(@RequestBody TeamDto.Request request){
        return teamRepository.save(Team.builder()
        .name(request.getName())
        .leader(request.getLeader())
        .build());
    }

    //팀 수정(이름)
    public TeamDto.Response updateTeam(Team team, String name) {
        if(teamRepository.findByNameAndLeader(team.getName(),team.getLeader())){
            team.updateTeam(name);
            return new TeamDto.Response(teamRepository.save(team));
        }
        return null;
    }

    //팀삭제
    public boolean deleteTeam(Team team){
        if(teamRepository.findByNameAndLeader(team.getName(),team.getLeader())){
            teamRepository.delete(team);
            return true;
        }
        return false;
    }
}
