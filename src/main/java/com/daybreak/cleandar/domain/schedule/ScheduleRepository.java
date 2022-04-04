package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findScheduleById(Long id);

    List<Schedule> findByUserInAndEndGreaterThanAndStartLessThan(List<User> users, LocalDateTime start, LocalDateTime end);

    List<Schedule> findByUser(User user);
}
