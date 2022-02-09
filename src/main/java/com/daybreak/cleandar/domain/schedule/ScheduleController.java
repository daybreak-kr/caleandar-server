package com.daybreak.cleandar.domain.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleDto.Response addSchedule(Principal principal, @RequestBody ScheduleDto.Request request){
        Schedule schedule = scheduleService.addSchedule(principal.getName(), request);
        return new ScheduleDto.Response(schedule);
    }

}
