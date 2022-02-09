package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public Schedule addSchedule(String email, ScheduleDto.Request request) {
        User user = userRepository.findUserByEmail(email);
        Schedule schedule = request.toEntity(user);
        return scheduleRepository.save(schedule);
    }

}
