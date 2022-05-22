package com.daybreak.cleandar.domain.user;

import com.daybreak.cleandar.security.UserPrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserPrincipalDetailsService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "users/login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "users/new";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto.Request request) {
        User user = userService.create(request);
        if (user != null) {
            return "redirect:users/login";
        } else {
            return "redirect:users/new";
        }
    }

    // TODO 미사용 코드
    // @GetMapping("/users")
    // public ModelAndView updateForm(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    //     ModelAndView mav = new ModelAndView("user");
    //     mav.addObject("userDto", new UserDto.Response(userPrincipal.getUser()));
    //     return mav;
    // }
}
