package com.npee.myproject.domain.entity.dto;

import com.npee.myproject.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RequestUserDto {

    @Getter
    @NoArgsConstructor
    public static class CreateUser {
        private String userLoginId;
        private String password;
        private String userName;

        public User toEntity() {
            return User.builder()
                    .userLoginId(this.userLoginId)
                    .userName(this.userName)
                    .password(this.password)
                    .build();
        }
    }
}
