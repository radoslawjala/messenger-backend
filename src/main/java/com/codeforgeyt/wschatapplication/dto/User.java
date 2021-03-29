package com.codeforgeyt.wschatapplication.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    private String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }
}
