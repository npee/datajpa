package com.npee.myproject.repository;

import com.npee.myproject.domain.entity.Member;
import com.npee.myproject.domain.entity.Team;
import com.npee.myproject.domain.entity.dto.MemberTeamDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    TeamRepository teamRepository;

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

    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("AAA", 20, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void namedQueryTest() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 20, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    void namedQueryAnnotationTest() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 20, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    void findUsernameListTest() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 20, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        assertThat(result.get(0)).isEqualTo("AAA");
        assertThat(result.get(1)).isEqualTo("BBB");
    }

    @Test
    void findMemberTeamDto() {
        Member m1 = new Member("AAA", 10, null);
        Team t1 = new Team("TeamA");
        m1.setTeam(t1);
        Member m2 = new Member("BBB", 20, null);
        Team t2 = new Team("TeamB");
        m2.setTeam(t2);
        memberRepository.save(m1);
        teamRepository.save(t1);
        memberRepository.save(m2);
        teamRepository.save(t2);

        List<MemberTeamDto> memberTeam = memberRepository.findMemberTeam();

        assertThat(memberTeam.get(0).getId()).isEqualTo(m1.getId());
        assertThat(memberTeam.get(0).getUsername()).isEqualTo(m1.getUsername());
        assertThat(memberTeam.get(0).getTeamName()).isEqualTo(t1.getName());
    }

    @Test
    void findByNamesTest() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 20, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> namesList = Arrays.asList("AAA", "BBB");

        List<Member> result = memberRepository.findByNames(namesList);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(1).getUsername()).isEqualTo("BBB");
    }

    @Test
    void returnTest() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 20, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> list = memberRepository.findListByUsername("AAA");
        Member member = memberRepository.findMemberByUsername("BBB");
        Optional<Member> optional = memberRepository.findOptionalByUsername("AAA");

        assertThat(list.get(0)).isEqualTo(m1);
        assertThat(member).isEqualTo(m2);
        assertThat(optional.get()).isEqualTo(m1);
    }

    @Test
    public void paging() {
        // given
        for (int i = 1; i <= 5; i++) {
            memberRepository.save(new Member("member" + i, 10, null));
        }

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }
}