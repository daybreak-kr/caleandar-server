package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import com.daybreak.cleandar.domain.user.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleDto {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        private Long id;
        private String start;
        private String end;
        private String title;
        private String description;

        @Builder
        public Request(Long id, String start, String end, String title, String description) {
            this.id = id;
            this.start = start;
            this.end = end;
            this.title = title;
            this.description = description;
        }

        public Schedule toEntity(User user) {
            return Schedule.builder()
                    .start(LocalDateTime.parse(start))
                    .end(LocalDateTime.parse(end))
                    .title(title)
                    .description(description)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long teamId;
        private String start;
        private String end;
        private String createAt;
        private String updateAt;
        private String title;
        private String description;
        private UserDto.Response user;

        public Response(Schedule schedule) {
            id = schedule.getId();
            start = schedule.getStart().format(formatter);
            end = schedule.getEnd().format(formatter);
            createAt = schedule.getCreatedAt().format(formatter);
            updateAt = schedule.getUpdatedAt().format(formatter);
            title = schedule.getTitle();
            description = schedule.getDescription();
            user = new UserDto.Response(schedule.getUser());
        }

        public Response(String start, String end) {
            this.start = start;
            this.end = end;
        }

        public Response(Long teamId, String start, String end) {
            this.teamId = teamId;
            this.start = start;
            this.end = end;
        }

    }
}
