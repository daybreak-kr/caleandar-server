package com.daybreak.cleandar.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserDto {

    @Getter
    @Setter
    public static class Request {
        private Long id;
        private String email;
        private String password;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String email;
        private String name;

        public Response(User user) {
            id = user.getId();
            email = user.getEmail();
            name = user.getName();
        }
    }
}
