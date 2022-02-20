package com.daybreak.cleandar.domain.team;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public TeamDto.Response updateTeam(Team team, @RequestBody TeamDto.Request request){
        return teamService.updateTeam(request);
    }

}
