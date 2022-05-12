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
        mav.addObject("schedule", scheduleService.getOne(id));
        mav.setStatus(HttpStatus.OK);
        return mav;
    }

    @GetMapping("")
    public ModelAndView getAllSchedule(@AuthenticationPrincipal UserPrincipal principal) {
        ModelAndView mav = new ModelAndView("schedules/list");
        mav.addObject("schedules", scheduleService.getAll(principal.getUsername()));
        mav.setStatus(HttpStatus.OK);

        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getSchedule(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("schedules/show");
        mav.addObject("schedule", scheduleService.getOne(id));
        mav.setStatus(HttpStatus.OK);
        return mav;
    }

    @GetMapping("/candidates")
    public ModelAndView getCandidates(String start, String end, Long teamId) {
        ModelAndView mav = new ModelAndView("teams/candidates");
        mav.addObject("candidates", scheduleService.getCandidateSchedules(LocalDateTime.parse(start), LocalDateTime.parse(end), teamId));
        mav.setStatus(HttpStatus.OK);
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
        mav.setStatus(HttpStatus.CREATED);
        return mav;
    }

    @DeleteMapping("/{id}")
    public String deleteSchedule(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        if (scheduleService.delete(principal.getUsername(), id))
            return "redirect:/schedules";
        else
            return "삭제 실패";
    }

    @PutMapping("/{id}")
    public ModelAndView updateSchedule(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, ScheduleDto.Request request) {
        ModelAndView mav = new ModelAndView("schedules/show");
        ScheduleDto.Response schedule = scheduleService.update(principal.getUsername(), request);
        mav.addObject("schedule", schedule);
        mav.setStatus(HttpStatus.OK);
        return mav;
    }
}
