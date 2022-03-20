package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/new")
    public String createScheduleForm() {
        return "schedules/new";
    }

    @GetMapping("/{id}/edit")
    public String editScheduleForm() {
        return "schedules/edit";
    }

    @GetMapping("")
    public ModelAndView getAllSchedule(@AuthenticationPrincipal UserPrincipal principal) {
        ModelAndView mav = new ModelAndView("schedules/list");
        List<ScheduleDto.Response> list = new ArrayList<>();

        for (Schedule schedule : scheduleService.getAll(principal.getUsername())) {
            list.add(new ScheduleDto.Response(schedule));
        }

        mav.addObject("schedules", list);
        mav.setStatus(HttpStatus.OK);

        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getSchedule(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("schedules/detail");
        mav.addObject("schedule", new ScheduleDto.Response(scheduleService.getOne(id)));
        mav.setStatus(HttpStatus.OK);
        return mav;
    }

    @GetMapping("/candidates")
    public ModelAndView getCandidates(String start, String end, Long teamId) {
        ModelAndView mav = new ModelAndView("redirect:/");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        mav.addObject("candidates", scheduleService.getCandidateSchedules(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter), teamId));
        mav.setStatus(HttpStatus.OK);
        return mav;
    }

    @PostMapping("/new")
    public ModelAndView createSchedule(@AuthenticationPrincipal UserPrincipal principal, ScheduleDto.Request request) {
        ModelAndView mav = new ModelAndView("schedules/detail");
        Schedule schedule = scheduleService.create(principal.getUser(), request);
        mav.addObject("schedule", new ScheduleDto.Response(schedule));
        mav.setStatus(HttpStatus.CREATED);
        return mav;
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteSchedule(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        ModelAndView mav = new ModelAndView("schedules/list");
        mav.addObject("result", scheduleService.delete(principal.getUsername(), id));
        mav.setStatus(HttpStatus.OK);
        return mav;
    }

    //TODO view name 수정
    @PutMapping("/{id}")
    public ModelAndView updateSchedule(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, ScheduleDto.Request request) {
        ModelAndView mav = new ModelAndView("schedules/detail");
        Schedule schedule = scheduleService.update(principal.getUsername(), request);
//        String url = "redirect:/schedules/" + schedule.getId().toString();
//        mav.setViewName(url);
        mav.addObject("schedule", new ScheduleDto.Response(schedule));
        mav.setStatus(HttpStatus.OK);
        return mav;
    }


    @PostMapping("/team")
    public ModelAndView createTeamSchedule(@AuthenticationPrincipal UserPrincipal principal, ScheduleDto.Request request, Long teamId) {
        ModelAndView mav = new ModelAndView("schedules/detail");
        mav.addObject("teamSchedule", scheduleService.createTeamSchedule(principal.getUser(), request, teamId));
        mav.setStatus(HttpStatus.CREATED);
        return mav;
    }
}
