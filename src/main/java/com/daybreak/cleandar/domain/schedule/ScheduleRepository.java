package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserInAndStartLessThanAndEndGreaterThan(List<User> users, LocalDateTime end, LocalDateTime start);
}
