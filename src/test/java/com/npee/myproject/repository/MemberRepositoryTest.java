package com.npee.myproject.repository;

import com.npee.myproject.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId())
                .isEqualTo(member.getId());
        assertThat(findMember.getUsername())
                .isEqualTo(member.getUsername());
        assertThat(findMember)
                .isEqualTo(member);

    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        member1.setUsername("member3"); // Transaction 내에 있으므로 변경 감지 일어남

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2L);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2L);

//        memberJpaRepository.delete(member1);
//        memberJpaRepository.delete(member2);
//
//        long resCount = memberJpaRepository.count();
//        assertThat(resCount).isEqualTo(0L);
    }

}