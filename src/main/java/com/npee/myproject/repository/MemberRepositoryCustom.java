package com.npee.myproject.repository;

import com.npee.myproject.domain.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
