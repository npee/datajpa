package com.npee.myproject.repository;

public class UsernameOnlyDto {

    private final String username;

    public UsernameOnlyDto(String username) { // parameter 이름으로 매핑
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
