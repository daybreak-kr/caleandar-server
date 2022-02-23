package com.daybreak.cleandar.domain.user;

import com.daybreak.cleandar.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.Response createUser(@RequestBody UserDto.Request request) {
        User user = userService.create(request);
        return new UserDto.Response(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto.Response updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @RequestBody UserDto.Request request) {
        return userService.update(userPrincipal.getUser(), request);
    }
}
