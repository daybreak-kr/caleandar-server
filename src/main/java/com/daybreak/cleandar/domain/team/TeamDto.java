package com.daybreak.cleandar.domain.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class TeamDto {

    @Getter
    public static class Request {
        private Long id;
        private String name;
        private String leader;

        @Builder
        public Request(Long id, String name, String leader) {
            this.id = id;
            this.name = name;
            this.leader = leader;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private Long id;
        private String name;
        private String leader;

        public Response(Team team) {
            id = team.getId();
            name = team.getName();
            leader = team.getLeader();
        }
    }
}
