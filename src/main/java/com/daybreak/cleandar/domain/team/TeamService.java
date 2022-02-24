package com.daybreak.cleandar.domain.team;

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
    //@Transactional
    public TeamDto.Response updateTeam(@RequestBody TeamDto.Request request) {

        Team team =teamRepository.findByNameAndLeader(request.getName(), request.getLeader()).orElseThrow(() ->
                new IllegalArgumentException("해당 되는 조건이 없습니다."));

        team.updateTeam(request.getName());

        return new TeamDto.Response(teamRepository.save(team));
    }
}
