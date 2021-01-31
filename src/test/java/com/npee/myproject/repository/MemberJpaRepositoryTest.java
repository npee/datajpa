package com.npee.myproject.repository;

import com.npee.myproject.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Java6AbstractBDDSoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false) // 쿼리 확인
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(member.getId());

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

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        member1.setUsername("member3"); // Transaction 내에 있으므로 변경 감지 일어남

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2L);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2L);

//        memberJpaRepository.delete(member1);
//        memberJpaRepository.delete(member2);
//
//        long resCount = memberJpaRepository.count();
//        assertThat(resCount).isEqualTo(0L);
    }


}