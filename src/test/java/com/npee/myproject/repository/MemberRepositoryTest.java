package com.npee.myproject.repository;

import com.npee.myproject.domain.entity.Member;
import com.npee.myproject.domain.entity.Team;
import com.npee.myproject.domain.entity.dto.MemberTeamDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;
    
    @PersistenceContext
    EntityManager em;

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

        Page<MemberTeamDto> toMap = page.map(member -> new MemberTeamDto(member.getId(), member.getUsername(), null));

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

    @Test
    public void slicing() {
        // List/Page/Slice 반환 타입에 따라 쿼리가 결정된다.
        // given
        for (int i = 1; i <= 5; i++) {
            memberRepository.save(new Member("member" + i, 10, null));
        }

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Slice<Member> slice = memberRepository.getByAge(age, pageRequest);

        // then
        List<Member> content = slice.getContent();
        // long totalElements = slice.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        // assertThat(totalElements).isEqualTo(5);
        // assertThat(slice.getTotalPages()).isEqualTo(2);
        assertThat(slice.getNumber()).isEqualTo(0);
        assertThat(slice.isFirst()).isTrue();
        assertThat(slice.hasNext()).isTrue();

    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 16, null));
        memberRepository.save(new Member("member2", 19, null));
        memberRepository.save(new Member("member3", 20, null));
        memberRepository.save(new Member("member4", 21, null));
        memberRepository.save(new Member("member5", 25, null));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member.getAge = " + member.getAge());
        }

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // member:team -> lazy loading
        em.flush();
        em.clear();

        // when
        // List<Member> members = memberRepository.findAll();
        List<Member> members = memberRepository.getByUsername("member1");

        // N + 1
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10, null));
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.queryByUsername("member1");
        findMember.setUsername("member2");

        em.flush(); // readOnly이므로 변경감지 일어나지 않

    }

    @Test
    public void lockTest() {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10, null));
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void specBasic() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void queryByExample() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        // Probe
        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team); // 연관 관계 지정

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("m1");

    }

    @Test
    public void projections() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1", NestedClosedProjections.class);

        for (NestedClosedProjections userNameOnly : result) {
            System.out.println("usernameOnly = " + userNameOnly.getUsername());
        }
    }

    @Test
    public void nativeQuery() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Member result = memberRepository.findByNativeQuery("m1");
        System.out.println("result = " + result);
    }

}