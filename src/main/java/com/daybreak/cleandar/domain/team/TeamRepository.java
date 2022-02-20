package com.daybreak.cleandar.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;

//JPA로 DB 직접 접근하는 계층
//JpaRepository<사용될 EntityClass,ID>
public interface TeamRepository  extends JpaRepository<Team, Long> {

    Team findTeamByName(String name, String leader);

}
