package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("new")
    public ModelAndView teamForm() {
        ModelAndView mav = new ModelAndView("teams/new");
        return mav;
    }

    @PostMapping
    public ModelAndView create(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute TeamDto.Request request) {
        ModelAndView mav = new ModelAndView("redirect:/");
        request.setLeader(userPrincipal.getUser());
        Optional<Team> team = Optional.ofNullable(teamService.create(request));
        if (team.isPresent()) {
            mav.addObject("team", new TeamDto.Response(team.get()));
            mav.setStatus(HttpStatus.CREATED);
        } else {
            mav.setStatus(HttpStatus.BAD_REQUEST);
        }
        return mav;
    }
}
