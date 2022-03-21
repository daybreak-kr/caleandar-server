package com.daybreak.cleandar.domain.team;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/teams")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDto.Response createTeam(@RequestBody TeamDto.Request request) {
        Team team = teamService.createTeam(request);

        return new TeamDto.Response(team);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TeamDto.Response updateTeam(Team team, String name) {
        return teamService.updateTeam(team, name);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteTeam(Team team) {
        return teamService.deleteTeam(team);
    }
}
