package com.daybreak.cleandar;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamDto;
import com.daybreak.cleandar.domain.team.TeamRepository;
import com.daybreak.cleandar.domain.team.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    //@Transactional
    void createTeam(){
        //given
        String name = "team1";
        String leader = "ejw";

        //when
        TeamDto.Request request = TeamDto.Request.builder()
                .name(name)
                .leader(leader)
                .build();

        Team team = teamService.createTeam(request);

        //then
        /*Assertions.assertNotNull(team.getId());
        Assertions.assertEquals(name,team.getName());
        Assertions.assertEquals(leader,team.getLeader());*/

        //팀 생성
        teamRepository.save(team);
    }

    @Test
    void update(){
        String name = "team5";
        String leader = "ejw";
        String changeName = "team3";

        TeamDto.Request request = TeamDto.Request.builder()
                .name(name)
                .leader(leader)
                .build();

        Optional<Team> team = teamService.findByNameAndLeader(request);

        team.ifPresent(selectTeam->{
           TeamDto.Request changeRequest = TeamDto.Request.builder()
                    .name(changeName)
                    .build();
           //update 에러
            teamService.updateTeam(changeRequest);
        });
    }
}
