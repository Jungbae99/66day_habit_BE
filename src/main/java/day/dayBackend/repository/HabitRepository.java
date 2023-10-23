package day.dayBackend.repository;

import day.dayBackend.domain.habit.Habit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    Optional<Habit> findByIdAndDeletedAtNull(Long id);
    Optional<Habit> findByHabitNameAndDeletedAtNull(String habitName);

    // 유명한 습관 : 관리자의 추가
    @Query(value = "select h from Habit h " +
            "join h.member m " +
            "join m.memberAuthorities ma " +
            "join ma.authority a " +
            "where a.authorityName = 'ROLE_ADMIN' and h.deletedAt is null")
    Page<Habit> findFamousHabit(Pageable pageable);

    // 최신 습관
    @Query(value = "select h from Habit h where h.deletedAt is null order by h.createdAt desc")
    Page<Habit> findNewestHabit(Pageable pageable);

    // 완료한 습관
    @Query(value = "select h from Habit h " +
            "where (select count(hr) from h.habitRecords hr) = 66 " +
            "and h.deletedAt is null order by h.createdAt desc")
    Page<Habit> findDoneHabit(Pageable pageable);

}
