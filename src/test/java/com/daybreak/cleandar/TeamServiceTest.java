package com.daybreak.cleandar;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamDto;
import com.daybreak.cleandar.domain.team.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Test
    @Transactional
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
        Assertions.assertNotNull(team.getId());
        Assertions.assertEquals(name,team.getName());
        Assertions.assertEquals(leader,team.getLeader());

    }
}
