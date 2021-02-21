package com.npee.myproject.domain.entity.dto;

import com.npee.myproject.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberTeamDto {
    private Long id;
    private String username;
    private String teamName;

    public MemberTeamDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        if (!(member.getTeam() == null))
            this.teamName = member.getTeam().getName();
    }
}
