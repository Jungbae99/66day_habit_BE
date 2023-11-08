package day.dayBackend.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import day.dayBackend.domain.Member;
import day.dayBackend.domain.QMember;
import day.dayBackend.repository.MemberRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public Optional<Member> findBySearchAndDeletedAtNull(String search) {

        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember member = QMember.member;

        BooleanExpression searchCondition = member.email.eq(search).or(member.username.eq(search));

        Member result = queryFactory.selectFrom(member)
                .leftJoin(member.profileImage).fetchJoin()
                .leftJoin(member.backgroundImage).fetchJoin()
                .where(searchCondition.and(member.deletedAt.isNull()))
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
