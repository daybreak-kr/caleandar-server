package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleDto.Response createSchedule(@AuthenticationPrincipal UserPrincipal principal, @RequestBody ScheduleDto.Request request) {
        Schedule schedule = scheduleService.create(principal.getUser(), request);
        return new ScheduleDto.Response(schedule);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteSchedule(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {

        return scheduleService.delete(principal.getUsername(), id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleDto.Response updateSchedule(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, @RequestBody ScheduleDto.Request request) {
        Schedule schedule = scheduleService.update(principal.getUsername(), request);
        return new ScheduleDto.Response(schedule);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleDto.Response> getAllSchedule(@AuthenticationPrincipal UserPrincipal principal) {
        List<ScheduleDto.Response> list = new ArrayList<>();

        for (Schedule schedule : scheduleService.getAll(principal.getUsername())) {
            list.add(new ScheduleDto.Response(schedule));
        }

        return list;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleDto.Response getSchedule(@PathVariable Long id) {
        return new ScheduleDto.Response(scheduleService.getOne(id));
    }


}
