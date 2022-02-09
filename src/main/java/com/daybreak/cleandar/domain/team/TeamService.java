package com.daybreak.cleandar.domain.team;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

}
