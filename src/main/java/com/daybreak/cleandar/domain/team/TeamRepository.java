package com.daybreak.cleandar.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Table;
import java.util.Optional;

//JPA로 DB 직접 접근하는 계층
//JpaRepository<사용될 EntityClass,ID>
public interface TeamRepository  extends JpaRepository<Team, Long> {

    //쿼리 안쓰려고 했지만, 나중에 수정
    @Query("select t from Team t where name = :name and leader = :leader")
    Optional<Team> findByNameAndLeader(@Param("name") String name,@Param("leader") String leader);

   //Optional<Team> delete
}
