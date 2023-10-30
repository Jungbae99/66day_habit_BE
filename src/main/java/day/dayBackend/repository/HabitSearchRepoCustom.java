package day.dayBackend.repository;

import day.dayBackend.domain.habit.Habit;
import day.dayBackend.search.HabitSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

//TODO: Querydsl 용 레포지토리 커스텀 인터페이스입니다.
// Search, 동적 쿼리 생성 시 이곳에 정의하고 Impl 에서 구현합니다.
@Repository
public interface HabitSearchRepoCustom {

    Page<Habit> findByDeletedAtNull(Pageable pageable, HabitSearch search);
}
