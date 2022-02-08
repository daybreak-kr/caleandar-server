package com.daybreak.cleandar.domain.user;

import lombok.*;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private Long id;
        private String email;
        private String password;
        private String name;

        @Builder
        public Request(Long id, String email, String password, String name) {
            this.id = id;
            this.email = email;
            this.password = password;
            this.name = name;
        }
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
