package com.daybreak.cleandar.domain.schedule;

import com.daybreak.cleandar.domain.user.User;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Builder
    public Schedule(LocalDateTime start, LocalDateTime end,
                    String title, String description,
                    User user) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.user = user;
        this.user.getSchedules().add(this);
    }

    public void update(ScheduleDto.Request request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        this.start = LocalDateTime.parse(request.getStart(), formatter);
        this.end = LocalDateTime.parse(request.getEnd(), formatter);
        this.title = request.getTitle();
        this.description = request.getDescription();
    }
}
