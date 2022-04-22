package com.daybreak.cleandar.domain.team;

import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserDto;
import lombok.*;

public class TeamDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        private Long id;
        private String name;
        private User leader;

        @Builder
        public Request(Long id, String name, User leader) {
            this.id = id;
            this.name = name;
            this.leader = leader;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private UserDto.Response leader;

        public Response(Team team) {
            id = team.getId();
            name = team.getName();
            leader = new UserDto.Response(team.getLeader());
        }
    }
}
