package com.npee.myproject.repository;

import com.npee.myproject.domain.entity.Member;
import com.npee.myproject.domain.entity.Team;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

public class MemberSpec {

    public static Specification<Member> teamName(final String teamName) {
        // toPredicate
        return (Specification<Member>) (root, query, builder) -> {

            if (StringUtils.isEmpty(teamName)) {
                return null;
            }

            Join<Member, Team> t = root.join("team", JoinType.INNER); // join
            return builder.equal(t.get("name"), teamName);
        };
    }

    public static Specification<Member> username(final String username) {
        return (Specification<Member>) (root, query, builder) ->
            builder.equal(root.get("username"), username);
    }
}
