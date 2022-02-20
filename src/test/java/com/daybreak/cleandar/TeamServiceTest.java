package com.daybreak.cleandar;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamDto;
import com.daybreak.cleandar.domain.team.TeamRepository;
import com.daybreak.cleandar.domain.team.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
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
        String name = "team1";
        String leader = "ejw";

        Optional<Team> team = Optional.ofNullable(teamRepository.findTeamByName(name, leader));

        System.out.println(team);


        /*team.ifPresent(selectTeam->{
            selectTeam.builder()
                    .name("team3")
                    .leader("ejw");
            teamRepository.save(selectTeam);
        });*/

    }
}
