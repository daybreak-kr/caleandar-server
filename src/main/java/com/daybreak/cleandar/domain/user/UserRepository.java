package com.daybreak.cleandar.domain.user;

import com.daybreak.cleandar.domain.teamuser.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    List<User> findByTeamUserIn(List<TeamUser> teamUsers);

    List<User> findAllByNameStartingWithOrEmailStartingWith(String word, String sameWord);
}
