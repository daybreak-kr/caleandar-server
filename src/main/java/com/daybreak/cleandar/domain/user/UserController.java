package com.daybreak.cleandar.domain.user;

import com.daybreak.cleandar.security.UserPrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserPrincipalDetailsService userService;

    @GetMapping("/login")
    public ModelAndView loginForm() {
        ModelAndView mav = new ModelAndView("users/login");
        return mav;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "users/new";
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute UserDto.Request request) {
        ModelAndView mav = new ModelAndView("redirect:/");
        User user = userService.create(request);
        mav.addObject("user", new UserDto.Response(user));
        mav.setStatus(HttpStatus.CREATED);
        return mav;
    }

    // TODO 미사용 코드
    // @GetMapping("/users")
    // public ModelAndView updateForm(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    //     ModelAndView mav = new ModelAndView("user");
    //     mav.addObject("userDto", new UserDto.Response(userPrincipal.getUser()));
    //     return mav;
    // }
}
