package com.daybreak.cleandar.domain.team;


import lombok.Getter;
import lombok.Setter;

public class TeamDto {

    @Getter
    @Setter
    public static class Request{
        private String name;
        private String leader;
    }
    //response
}
