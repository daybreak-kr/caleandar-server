package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ModelAndView index(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        ModelAndView mav = new ModelAndView("teams/index");
        List<Team> teams = teamService.index(userPrincipal.getUser());
        mav.addObject("teams", teams);
        return mav;
    }

    @GetMapping("{id}")
    public ModelAndView show(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("teams/show");
        Team team = teamService.show(id);
        if (team == null) {
            mav.setStatus(HttpStatus.NOT_FOUND);
        } else {
            mav.addObject("team", team);
            mav.setStatus(HttpStatus.OK);
        }
        return mav;
    }

    @GetMapping("new")
    public ModelAndView teamForm() {
        ModelAndView mav = new ModelAndView("teams/new");
        return mav;
    }

    @GetMapping("{id}/edit")
    public ModelAndView teamEditForm(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("teams/edit");
        Team team = teamService.show(id);
        if (team == null) {
            mav.setStatus(HttpStatus.NOT_FOUND);
        } else {
            mav.addObject("team", team);
            mav.setStatus(HttpStatus.OK);
        }
        return mav;
    }

    @PostMapping
    public String create(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute TeamDto.Request request) {
        request.setLeader(userPrincipal.getUser());
        Optional<Team> team = Optional.ofNullable(teamService.create(request));
        if (team.isPresent()) {
            return "redirect:teams";
        } else {
            return "redirect:new";
        }
    }

    @PutMapping("{id}")
    public String update(@ModelAttribute TeamDto.Request request) {
        Team team = teamService.update(request);
        if (team == null) {
            return "redirect:edit";
        } else {
            return "redirect:" + team.getId();
        }
    }

    @DeleteMapping("{id}")
    public String delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id) {
        teamService.delete(userPrincipal.getUser(), id);
        return "redirect:/teams";
    }
}
