package com.daybreak.cleandar.domain.team;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/teams")
//모든 도메인,접근 허용
@CrossOrigin(origins = "*")
//의존성 주입
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //json >> java Object
    public TeamDto.Response createTeam(@RequestBody TeamDto.Request request) {

        Team team = teamService.createTeam(request);

        return new TeamDto.Response(team);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TeamDto.Response updateTeam(@RequestBody TeamDto.Request request){
        //먼저 리더와 이름으로 조회해서 있으면 팀 이름 수정 가능케
        Optional<Team> team = teamService.findByNameAndLeader(request);
        
        return teamService.updateTeam(request);
    }

}
