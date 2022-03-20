package com.daybreak.cleandar.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByNameAndLeader(String name, String leader);
    Team findByNameAndLeader(String name, String leader);
}
