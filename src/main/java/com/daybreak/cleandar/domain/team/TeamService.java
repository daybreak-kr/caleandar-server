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
    //@Transactional
    public TeamDto.Response updateTeam(@RequestBody TeamDto.Request request) {
        /*Team team =teamRepository.findByNameAndLeader(request.getName(), request.getLeader()).orElseThrow(() ->
                new IllegalArgumentException("해당 되는 조건이 없습니다."));*/
        Team team = new Team();

        team.updateTeam(request.getName());

        return new TeamDto.Response(teamRepository.save(team));
    }

    //팀 삭제
    /*public TeamDto.Response deleteTeam(@RequestBody TeamDto.Request request) {
        return new TeamDto.Response(teamRepository.de)
    }*/

    //팀 이름,리더로 조회
    public Optional<Team> findByNameAndLeader(@RequestBody TeamDto.Request request){
        return teamRepository.findByNameAndLeader(request.getName(), request.getLeader());
    }

}
