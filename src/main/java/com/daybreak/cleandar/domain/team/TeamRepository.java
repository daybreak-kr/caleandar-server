package com.daybreak.cleandar.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository  extends JpaRepository<Team, Long> {
   boolean existsByNameAndLeader(String name, String leader);
}
