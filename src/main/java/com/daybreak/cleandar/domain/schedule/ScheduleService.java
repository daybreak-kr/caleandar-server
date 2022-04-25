package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.team.Team;
import com.daybreak.cleandar.domain.team.TeamRepository;
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
    private final TeamRepository teamRepository;

    public ScheduleDto.Response create(User user, ScheduleDto.Request request) {
        Schedule schedule = request.toEntity(user);
        return new ScheduleDto.Response(scheduleRepository.save(schedule));
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

    public ScheduleDto.Response update(String email, ScheduleDto.Request request) {

        if (isAccessPossibleUser(email, request.getId())) {
            Schedule schedule = scheduleRepository.findById(request.getId())
                    .orElseThrow(IllegalArgumentException::new);
            schedule.update(request);
            return new ScheduleDto.Response(scheduleRepository.save(schedule));
        }
        return null;
    }

    public List<ScheduleDto.Response> getAll(String email) {
        List<ScheduleDto.Response> list = new ArrayList<>();
        for (Schedule schedule : userRepository.findUserByEmail(email).getSchedules()) {
            list.add(new ScheduleDto.Response(schedule));
        }
        return list;
    }

    public ScheduleDto.Response getOne(Long id) {
        return new ScheduleDto.Response(scheduleRepository.findById(id).orElse(null));
    }

    public List<ScheduleDto.Response> getCandidateSchedules(LocalDateTime startDate, LocalDateTime endDate, Long teamId) {

        List<ScheduleDto.Response> candidate = new ArrayList<>();
        List<ScheduleDto.Response> teamSchedules = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<TeamUser> teamUser = teamRepository.findById(teamId).get().getTeamUsers();
        List<User> users = userRepository.findByTeamUserIn(teamUser);
        List<Schedule> schedules = scheduleRepository.findByUserInAndEndGreaterThanAndStartLessThan(users, startDate, endDate);

        for (Schedule schedule : schedules) {
            candidate.add(new ScheduleDto.Response(schedule));
        }

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


    public ScheduleDto.Response createTeamSchedule(User user, ScheduleDto.Request request, Long teamId) {

        Team team = teamRepository.getById(teamId);
        if (user.getName().equals(team.getLeader())) { // TeamUser에서 leader의 type을 User로 바꾼다면 변경할 부분.
            for (TeamUser member : team.getTeamUsers()) {
                create(member.getUser(), request);
            }
        }
        return new ScheduleDto.Response(teamId, request.getStart(), request.getEnd());
    }
}
