package com.npee.myproject.domain.entity.dto;

import com.npee.myproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDto {
    private Long userId;
    private String userLoginId;
    private String userName;

    public ResponseUserDto(User user) {
        this.userId = user.getId();
        this.userLoginId = user.getUserLoginId();
        this.userName = user.getUserName();
    }
}
