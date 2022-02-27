package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public Schedule create(User user, ScheduleDto.Request request) {
        Schedule schedule = request.toEntity(user);
        return scheduleRepository.save(schedule);
    }

    public boolean delete(String email, Long id) {

        if (isAccessPossibleUser(email, id)) {
            scheduleRepository.deleteById(id);
        }
        return !scheduleRepository.existsById(id);
    }

    private boolean isAccessPossibleUser(String email, Long id) {
        return email.equals(scheduleRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new).getUser().getEmail());
    }

    public Schedule update(String email, ScheduleDto.Request request) {

        if (isAccessPossibleUser(email, request.getId())) {
            Schedule schedule = scheduleRepository.findById(request.getId())
                    .orElseThrow(IllegalArgumentException::new);
            schedule.update(request);
            return scheduleRepository.save(schedule);
        }
        return null;
    }

    public List<Schedule> getAll(String email) {
        return userRepository.findUserByEmail(email).getSchedules();
    }

    public Schedule getOne(Long id) {
        return scheduleRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
