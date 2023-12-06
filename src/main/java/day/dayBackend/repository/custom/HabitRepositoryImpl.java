package day.dayBackend.repository.custom;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import day.dayBackend.domain.habit.Habit;
import day.dayBackend.domain.habit.QHabit;
import day.dayBackend.repository.HabitRepositoryCustom;
import day.dayBackend.search.HabitSearch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

/**
 * 습관 검색을 위한 구현체
 */
@Repository
@RequiredArgsConstructor
public class HabitRepositoryImpl implements HabitRepositoryCustom {

    private final EntityManager em;

    @Override
    public Page<Habit> findByDeletedAtNull(Pageable pageable, HabitSearch search) {

        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        QHabit qHabit = QHabit.habit;

        JPQLQuery<Habit> query = queryFactory.selectFrom(qHabit)
                .where(
                        containsNameOrTag(qHabit, search.getKeyword1())
                                .and(containsNameOrTag(qHabit, search.getKeyword2()))
                )
                .orderBy(orderByCreatedAt(search.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }


    /**
     * 검색조건 1 BooleanExpression
     */
    private BooleanExpression containsNameOrTag(QHabit habit, String search) {
        if (search == null || search.isEmpty()) {
            return null;
        }
        BooleanExpression nameExpression = habit.habitName.like("%" + search + "%");
        BooleanExpression tagExpression = habit.habitTags.any().like("%" + search + "%");
        return nameExpression.or(tagExpression);
    }


    /**
     * 정렬조건은 OrderSpecifier
     */
    private OrderSpecifier<?> orderByCreatedAt(String sort) {
        if ("-createdAt".equals(sort)) {
            return QHabit.habit.createdAt.desc();
        }
        return QHabit.habit.createdAt.asc();
    }

}
