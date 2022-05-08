package com.daybreak.cleandar.domain.user;

import com.daybreak.cleandar.domain.schedule.Schedule;
import com.daybreak.cleandar.domain.teamuser.TeamUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Schedule> schedules = new ArrayList<>();

    //찾아볼내용 테이블과 테이블 연결시(N:M)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<TeamUser> teamUser = new ArrayList<>();

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
