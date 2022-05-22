package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;


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
    public ModelAndView editScheduleForm(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("schedules/edit");
        mav.addObject("schedule", scheduleService.getSchedule(id));
        return mav;
    }

    @GetMapping("")
    public ModelAndView getSchedules(@AuthenticationPrincipal UserPrincipal principal) {
        ModelAndView mav = new ModelAndView("schedules/index");
        mav.addObject("schedules", scheduleService.getSchedules(principal.getUsername()));
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getSchedule(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("schedules/show");
        mav.addObject("schedule", scheduleService.getSchedule(id));
        return mav;
    }

    @GetMapping("/candidates")
    public ModelAndView getCandidates(String start, String end, Long teamId) {
        ModelAndView mav = new ModelAndView("teams/candidates");
        mav.addObject("candidates", scheduleService.getCandidateSchedules(LocalDateTime.parse(start), LocalDateTime.parse(end), teamId));
        return mav;
    }

    @PostMapping("/new")
    public String createSchedule(@AuthenticationPrincipal UserPrincipal principal, ScheduleDto.Request request) {
        String url = "/schedules/" + scheduleService.create(principal.getUser(), request).getId();
        return "redirect:" + url;
    }

    @PostMapping("/team/{id}")
    public ModelAndView createTeamSchedule(@AuthenticationPrincipal UserPrincipal principal, ScheduleDto.Request request, @PathVariable(value = "id") Long teamId) {
        ModelAndView mav = new ModelAndView("teams/show");
        mav.addObject("teamSchedule", scheduleService.createTeamSchedule(principal.getUser(), request, teamId));
        return mav;
    }

    @DeleteMapping("/{id}")
    public String deleteSchedule(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        scheduleService.delete(principal.getUsername(), id);
        return "redirect:/schedules";
    }

    @PutMapping("/{id}")
    public ModelAndView updateSchedule(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, ScheduleDto.Request request) {
        ModelAndView mav = new ModelAndView("schedules/show");
        ScheduleDto.Response schedule = scheduleService.update(principal.getUsername(), request);
        mav.addObject("schedule", schedule);
        return mav;
    }
}
