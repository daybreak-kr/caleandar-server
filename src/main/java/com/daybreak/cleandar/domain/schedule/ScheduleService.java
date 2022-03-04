package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.teamuser.TeamUser;
import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public List<ScheduleDto.Response> getCandidateSchedules(LocalDateTime startDate, LocalDateTime endDate, List<TeamUser> teamUser) {

        List<ScheduleDto.Response> candidate = new ArrayList<>();
        List<ScheduleDto.Response> teamSchedules = new ArrayList<>();

        for (TeamUser member : teamUser) {
            for (Schedule schedule : member.getUser().getSchedules()) {
                if (startDate.isBefore(schedule.getEnd()) && endDate.isAfter(schedule.getStart()))
                    candidate.add(new ScheduleDto.Response(schedule));
            }
        }

        return compareSchedules(startDate, endDate, candidate, teamSchedules);
    }

    private List<ScheduleDto.Response> compareSchedules(LocalDateTime startDate, LocalDateTime endDate, List<ScheduleDto.Response> candidate, List<ScheduleDto.Response> teamSchedules) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (startDate.isBefore(LocalDateTime.parse(candidate.get(0).getStart(), formatter))) {
            teamSchedules.add(new ScheduleDto.Response(startDate.format(formatter), candidate.get(0).getStart()));
        }

        for (int i = 1; i < candidate.size(); i++) {
            if (candidate.get(i - 1).getEnd().equals(candidate.get(i).getStart())) {
                continue;
            }

            teamSchedules.add(new ScheduleDto.Response(candidate.get(i - 1).getEnd(), candidate.get(i).getStart()));
        }

        if (LocalDateTime.parse(candidate.get(candidate.size() - 1).getEnd(), formatter).isBefore(endDate)) {
            teamSchedules.add(new ScheduleDto.Response(candidate.get(candidate.size() - 1).getEnd(), endDate.format(formatter)));
        }

        return teamSchedules;
    }
}
