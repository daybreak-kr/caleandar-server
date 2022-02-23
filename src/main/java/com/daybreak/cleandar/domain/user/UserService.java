package com.daybreak.cleandar.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(UserDto.Request request) {
        return userRepository.save(User.builder()
                .email(request.getEmail())
                .password(new BCryptPasswordEncoder().encode(request.getPassword()))
                .name(request.getName())
                .build());
    }

    public UserDto.Response update(User user, UserDto.Request request) {
        user.updateName(request.getName());
        return new UserDto.Response(userRepository.save(user));
    }
}
