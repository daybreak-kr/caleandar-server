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
    void createTeam() {
        //given
        String name = "team1";
        String leader = "ejw";

        //when
        TeamDto.Request request = TeamDto.Request.builder().name(name).leader(leader).build();

        Team team = teamService.createTeam(request);

        //then
        /*Assertions.assertNotNull(team.getId());
        Assertions.assertEquals(name,team.getName());
        Assertions.assertEquals(leader,team.getLeader());*/

        //팀 생성
        teamRepository.save(team);
    }

    @Test
    void update() {
        //given
        String name = "team5";
        String leader = "ejw";
        String changeName = "team3";

        Team team = Team.builder().name(name).leader(leader).build();

        TeamDto.Request request = TeamDto.Request.builder().name(changeName).build();

        TeamDto.Response response = teamService.updateTeam(name, leader, request);

        //then
        Assertions.assertEquals(changeName, response.getName());
    }

    @Test
    @Transactional
    void delete() {
        //given
        String name = "team3";
        String leader = "ejw";

        //when
        boolean result = teamService.deleteTeam(name, leader, 5L);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    @Transactional
    void isAccessUser() {
        //given
        String email = "epsxkf01@naver.com";
        teamService.checkUserInformation(email);
    }
}
