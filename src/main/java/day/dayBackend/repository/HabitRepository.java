package day.dayBackend.repository;

import day.dayBackend.domain.HabitLikes;
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

    @Query(value = "select h from Habit h " +
            "join h.member m " +
            "join m.memberAuthorities ma " +
            "join ma.authority a " +
            "where a.authorityName = 'ROLE_ADMIN' and h.deletedAt is null")
    Page<Habit> findAllHabit(Pageable pageable);
}
