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
        System.out.println("memberJpaRepository = " + memberJpaRepository.count());
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


    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("AAA", 20, null);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void namedQueryTest() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 20, null);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void paging() {
        // given
        for (int i = 1; i <= 5; i++) {
            memberJpaRepository.save(new Member("member" + i, 10, null));
        }

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);

    }

    @Test
    public void bulkUpdate() {
        // given
        memberJpaRepository.save(new Member("member1", 16, null));
        memberJpaRepository.save(new Member("member1", 19, null));
        memberJpaRepository.save(new Member("member1", 20, null));
        memberJpaRepository.save(new Member("member1", 21, null));
        memberJpaRepository.save(new Member("member1", 25, null));

        // when
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        // then
        assertThat(resultCount).isEqualTo(3);
    }

}