package com.daybreak.cleandar.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository  extends JpaRepository<Team, Long> {
   boolean findByNameAndLeader(@Param("name") String name,@Param("leader") String leader);
}
