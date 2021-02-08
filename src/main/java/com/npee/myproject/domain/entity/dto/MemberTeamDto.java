package com.npee.myproject.domain.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberTeamDto {
    private Long id;
    private String username;
    private String teamName;
}
